package com.mercadopago.android.px.internal.datasource;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.addons.ESCManagerBehaviour;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.configuration.DiscountParamsConfiguration;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.datasource.cache.Cache;
import com.mercadopago.android.px.internal.repository.DisabledPaymentMethodRepository;
import com.mercadopago.android.px.internal.repository.ExperimentsRepository;
import com.mercadopago.android.px.internal.repository.InitRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.services.CheckoutService;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.internal.viewmodel.mappers.ExpressMetadataToDisabledIdMapper;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.internal.CheckoutFeatures;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import com.mercadopago.android.px.model.internal.InitRequest;
import com.mercadopago.android.px.model.internal.InitResponse;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mercadopago.android.px.services.BuildConfig.API_ENVIRONMENT_INIT;

public class InitService implements InitRepository {

    private static final int MAX_REFRESH_RETRIES = 4;
    private static final int RETRY_DELAY = 500;

    @NonNull private final ESCManagerBehaviour escManagerBehaviour;
    @NonNull private final CheckoutService checkoutService;
    @NonNull private final String language;
    @NonNull /* default */ final PaymentSettingRepository paymentSettingRepository;
    @NonNull /* default */ final ExperimentsRepository experimentsRepository;
    @NonNull /* default */ DisabledPaymentMethodRepository disabledPaymentMethodRepository;
    @NonNull /* default */ final Cache<InitResponse> initCache;
    @NonNull /* default */ final List<OnChangedListener> listeners = new ArrayList<>();
    @Nullable private final String flow;
    /* default */ int refreshRetriesAvailable = MAX_REFRESH_RETRIES;
    /* default */ Handler retryHandler;

    public InitService(@NonNull final PaymentSettingRepository paymentSettingRepository,
        @NonNull final ExperimentsRepository experimentsRepository,
        @NonNull final DisabledPaymentMethodRepository disabledPaymentMethodRepository,
        @NonNull final ESCManagerBehaviour escManagerBehaviour, @NonNull final CheckoutService checkoutService,
        @NonNull final String language, @Nullable final String flow, @NonNull final Cache<InitResponse> initCache) {
        this.paymentSettingRepository = paymentSettingRepository;
        this.experimentsRepository = experimentsRepository;
        this.disabledPaymentMethodRepository = disabledPaymentMethodRepository;
        this.escManagerBehaviour = escManagerBehaviour;
        this.checkoutService = checkoutService;
        this.language = language;
        this.flow = flow;
        this.initCache = initCache;
    }

    @NonNull
    @Override
    public MPCall<InitResponse> init() {
        return initCache.isCached() ? initCache.get() : newCall();
    }

    @NonNull
    private MPCall<InitResponse> newCall() {
        return new MPCall<InitResponse>() {

            @Override
            public void enqueue(final Callback<InitResponse> callback) {
                newRequest().enqueue(getInternalCallback(callback));
            }

            @Override
            public void execute(final Callback<InitResponse> callback) {
                newRequest().execute(getInternalCallback(callback));
            }

            @NonNull /* default */ Callback<InitResponse> getInternalCallback(
                final Callback<InitResponse> callback) {
                return new Callback<InitResponse>() {
                    @Override
                    public void success(final InitResponse initResponse) {
                        MPTracker.getInstance().hasExpressCheckout(initResponse.hasExpressCheckoutMetadata());
                        if (initResponse.getCheckoutPreference() != null) {
                            paymentSettingRepository.configure(initResponse.getCheckoutPreference());
                        }
                        paymentSettingRepository.configure(initResponse.getSite());
                        paymentSettingRepository.configure(initResponse.getCurrency());
                        paymentSettingRepository.configure(initResponse.getConfiguration());
                        experimentsRepository.configure(initResponse.getExperiments());

                        disabledPaymentMethodRepository.storeDisabledPaymentMethodsIds(
                            new ExpressMetadataToDisabledIdMapper().map(initResponse.getExpress()));

                        MPTracker.getInstance().setExperiments(experimentsRepository.getExperiments());

                        initCache.put(initResponse);
                        notifyListeners(initResponse);
                        callback.success(initResponse);
                    }

                    @Override
                    public void failure(final ApiException apiException) {
                        callback.failure(apiException);
                    }
                };
            }
        };
    }

    @NonNull
        /* default */ MPCall<InitResponse> newRequest() {
        final CheckoutPreference checkoutPreference = paymentSettingRepository.getCheckoutPreference();
        final PaymentConfiguration paymentConfiguration = paymentSettingRepository.getPaymentConfiguration();

        final AdvancedConfiguration advancedConfiguration = paymentSettingRepository.getAdvancedConfiguration();
        final DiscountParamsConfiguration discountParamsConfiguration =
            advancedConfiguration.getDiscountParamsConfiguration();

        final CheckoutFeatures features = new CheckoutFeatures.Builder()
            .setSplit(paymentConfiguration.getPaymentProcessor().supportsSplitPayment(checkoutPreference))
            .setExpress(advancedConfiguration.isExpressPaymentEnabled())
            .setOdrFlag(true)
            .build();

        final InitRequest initRequest = new InitRequest.Builder(paymentSettingRepository.getPublicKey())
            .setCardWithEsc(new ArrayList<>(escManagerBehaviour.getESCCardIds()))
            .setCharges(paymentConfiguration.getCharges())
            .setDiscountParamsConfiguration(discountParamsConfiguration)
            .setCheckoutFeatures(features)
            .setCheckoutPreference(checkoutPreference)
            .setFlow(flow)
            .build();

        final String preferenceId = paymentSettingRepository.getCheckoutPreferenceId();
        if (preferenceId != null) {
            return checkoutService
                .checkout(API_ENVIRONMENT_INIT, preferenceId, language, paymentSettingRepository.getPrivateKey(),
                    JsonUtil.getMapFromObject(initRequest));
        } else {
            return checkoutService.checkout(API_ENVIRONMENT_INIT, language, paymentSettingRepository.getPrivateKey(),
                JsonUtil.getMapFromObject(initRequest));
        }
    }

    @Override
    public MPCall<InitResponse> refresh() {
        return new MPCall<InitResponse>() {
            @Override
            public void enqueue(final Callback<InitResponse> callback) {
                init().enqueue(getRefreshCallback(callback));
            }

            @Override
            public void execute(final Callback<InitResponse> callback) {
                init().execute(getRefreshCallback(callback));
            }
        };
    }

    /* default */ Callback<InitResponse> getRefreshCallback(@NonNull final Callback<InitResponse> originalCallback) {
        final Map<String, DisabledPaymentMethod> disabledPaymentMethodMap =
            disabledPaymentMethodRepository.getDisabledPaymentMethods();
        return new Callback<InitResponse>() {
            @Override
            public void success(final InitResponse initResponse) {
                new ExpressMetadataSorter(initResponse.getExpress(), disabledPaymentMethodMap).sort();
                initCache.evict();
                initCache.put(initResponse);
                notifyListeners(initResponse);
                originalCallback.success(initResponse);
            }

            @Override
            public void failure(final ApiException apiException) {
                originalCallback.failure(apiException);
            }
        };
    }

    @Override
    public MPCall<InitResponse> refreshWithNewCard(@NonNull final String cardId) {
        return new MPCall<InitResponse>() {
            @Override
            public void enqueue(final Callback<InitResponse> callback) {
                initCache.evict();
                init().enqueue(getRefreshWithNewCardCallback(cardId, callback));
            }

            @Override
            public void execute(final Callback<InitResponse> callback) {
                initCache.evict();
                init().execute(getRefreshWithNewCardCallback(cardId, callback));
            }
        };
    }

    /* default */ Callback<InitResponse> getRefreshWithNewCardCallback(@NonNull final String cardId,
        @NonNull final Callback<InitResponse> callback) {
        final Map<String, DisabledPaymentMethod> disabledPaymentMethodMap =
            disabledPaymentMethodRepository.getDisabledPaymentMethods();
        return new Callback<InitResponse>() {
            @Override
            public void success(final InitResponse initResponse) {
                refreshRetriesAvailable--;
                for (final ExpressMetadata node : initResponse.getExpress()) {
                    if (node.isCard() && node.getCard().getId().equals(cardId)) {
                        refreshRetriesAvailable = MAX_REFRESH_RETRIES;
                        new ExpressMetadataSorter(initResponse.getExpress(), disabledPaymentMethodMap)
                            .setPrioritizedCardId(cardId).sort();
                        initCache.put(initResponse);
                        notifyListeners(initResponse);
                        callback.success(initResponse);
                        return;
                    }
                }
                if (refreshRetriesAvailable > 0) {
                    if (retryHandler == null) {
                        final HandlerThread thread = new HandlerThread("MyInitRetryThread");
                        thread.start();
                        retryHandler = new Handler(thread.getLooper());
                    }
                    retryHandler.postDelayed(() -> refreshWithNewCard(cardId).enqueue(callback), RETRY_DELAY);
                } else {
                    callback.failure(new ApiException());
                }
            }

            @Override
            public void failure(final ApiException apiException) {
                callback.failure(apiException);
            }
        };
    }

    @Override
    public void addOnChangedListener(@NonNull final OnChangedListener listener) {
        listeners.add(listener);
        if (initCache.isCached()) {
            initCache.get().enqueue(new Callback<InitResponse>() {
                @Override
                public void success(final InitResponse initResponse) {
                    listener.onInitResponseChanged(initResponse);
                }

                @Override
                public void failure(final ApiException apiException) {
                    //Shouldn't happen because it's cached
                }
            });
        }
    }

    /* default */ void notifyListeners(@NonNull final InitResponse initResponse) {
        for (final OnChangedListener listener : listeners) {
            listener.onInitResponseChanged(initResponse);
        }
    }
}
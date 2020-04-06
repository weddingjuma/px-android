package com.mercadopago.android.px.internal.features.checkout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadolibre.android.cardform.internal.LifecycleListener;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.callbacks.FailureRecovery;
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler;
import com.mercadopago.android.px.internal.configuration.InternalConfiguration;
import com.mercadopago.android.px.internal.navigation.DefaultPaymentMethodDriver;
import com.mercadopago.android.px.internal.navigation.OnChangePaymentMethodDriver;
import com.mercadopago.android.px.internal.repository.CongratsRepository;
import com.mercadopago.android.px.internal.repository.InitRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.PluginRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel;
import com.mercadopago.android.px.internal.viewmodel.CheckoutStateModel;
import com.mercadopago.android.px.internal.viewmodel.PaymentModel;
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction;
import com.mercadopago.android.px.internal.viewmodel.handlers.PaymentModelHandler;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Cause;
import com.mercadopago.android.px.model.Currency;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.model.internal.InitResponse;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.model.Reason;
import java.util.List;

public class CheckoutPresenter extends BasePresenter<Checkout.View> implements PaymentServiceHandler,
    PostPaymentAction.ActionController, Checkout.Actions {

    @NonNull /* default */ final CheckoutStateModel state;
    @NonNull /* default */ final PaymentRepository paymentRepository;
    @NonNull /* default */ final PaymentSettingRepository paymentSettingRepository;
    @NonNull /* default */ final UserSelectionRepository userSelectionRepository;
    @NonNull private final PluginRepository pluginRepository;
    @NonNull private final InitRepository initRepository;
    @NonNull private final CongratsRepository congratsRepository;
    @NonNull private final InternalConfiguration internalConfiguration;
    private transient FailureRecovery failureRecovery;

    /* default */ CheckoutPresenter(@NonNull final CheckoutStateModel persistentData,
        @NonNull final PaymentSettingRepository paymentSettingRepository,
        @NonNull final UserSelectionRepository userSelectionRepository,
        @NonNull final InitRepository initRepository,
        @NonNull final PluginRepository pluginRepository,
        @NonNull final PaymentRepository paymentRepository,
        @NonNull final CongratsRepository congratsRepository,
        @NonNull final InternalConfiguration internalConfiguration) {

        this.paymentSettingRepository = paymentSettingRepository;
        this.userSelectionRepository = userSelectionRepository;
        this.initRepository = initRepository;
        this.pluginRepository = pluginRepository;
        this.paymentRepository = paymentRepository;
        this.congratsRepository = congratsRepository;
        this.internalConfiguration = internalConfiguration;
        state = persistentData;
    }

    @NonNull
    public CheckoutStateModel getState() {
        return state;
    }

    @Override
    public void initialize() {
        getView().showProgress();
        if (isViewAttached()) {
            initRepository.init().enqueue(new Callback<InitResponse>() {
                @Override
                public void success(final InitResponse initResponse) {
                    if (isViewAttached()) {
                        startFlow(initResponse);
                    }
                }

                @Override
                public void failure(final ApiException apiException) {
                    if (isViewAttached()) {
                        getView().showError(
                            new MercadoPagoError(apiException, ApiUtil.RequestOrigin.POST_INIT));
                    }
                }
            });
        }
    }

    /* default */ void startFlow(final PaymentMethodSearch paymentMethodSearch) {

        new DefaultPaymentMethodDriver(paymentMethodSearch,
            paymentSettingRepository.getCheckoutPreference().getPaymentPreference())
            .drive(new DefaultPaymentMethodDriver.PaymentMethodDriverCallback() {
                @Override
                public void driveToCardVault(@NonNull final Card card) {
                    userSelectionRepository.select(card, null);
                    getView().showSavedCardFlow(card);
                }

                @Override
                public void driveToNewCardFlow(final String defaultPaymentTypeId) {
                    userSelectionRepository.select(defaultPaymentTypeId);
                    getView().showNewCardFlow();
                }

                @Override
                public void doNothing() {
                    noDefaultPaymentMethods(paymentMethodSearch);
                }
            });
    }

    /* default */ void noDefaultPaymentMethods(final PaymentMethodSearch paymentMethodSearch) {
        saveIsExpressCheckout(paymentMethodSearch);
        savePaymentMethodQuantity(paymentMethodSearch);

        if (state.isExpressCheckout) {
            getView().hideProgress();
            getView().showOneTap();
        } else {
            getView().showPaymentMethodSelection();
        }
    }

    @Override
    public void onErrorCancel(@Nullable final MercadoPagoError mercadoPagoError) {
        if (isIdentificationInvalidInPayment(mercadoPagoError)) {
            getView().showPaymentMethodSelection();
        } else {
            cancelCheckout();
        }
    }

    private boolean isIdentificationInvalidInPayment(@Nullable final MercadoPagoError mercadoPagoError) {
        boolean identificationInvalid = false;
        if (mercadoPagoError != null && mercadoPagoError.isApiException()) {
            final List<Cause> causeList = mercadoPagoError.getApiException().getCause();
            if (causeList != null && !causeList.isEmpty()) {
                final Cause cause = causeList.get(0);
                if (cause.getCode().equals(ApiException.ErrorCodes.INVALID_IDENTIFICATION_NUMBER)) {
                    identificationInvalid = true;
                }
            }
        }
        return identificationInvalid;
    }

    /* default */ void onPaymentMethodSelected() {
        if (shouldSkipUserConfirmation()) {
            getView().showPaymentProcessorWithAnimation();
        } else {
            getView().showReviewAndConfirm(isUniquePaymentMethod());
        }
    }

    /* default */ boolean shouldSkipUserConfirmation() {
        return paymentSettingRepository.getPaymentConfiguration().getPaymentProcessor().shouldSkipUserConfirmation();
    }

    private void resolvePaymentFailure(final MercadoPagoError mercadoPagoError) {
        if (mercadoPagoError != null && mercadoPagoError.isPaymentProcessing()) {
            final Currency currency = paymentSettingRepository.getCurrency();
            final PaymentResult paymentResult =
                new PaymentResult.Builder()
                    .setPaymentData(paymentRepository.getPaymentDataList())
                    .setPaymentStatus(Payment.StatusCodes.STATUS_IN_PROCESS)
                    .setPaymentStatusDetail(Payment.StatusDetail.STATUS_DETAIL_PENDING_CONTINGENCY)
                    .build();
            final PaymentModel paymentModel = new PaymentModel(paymentResult, currency);
            getView().showPaymentResult(paymentModel);
        } else if (mercadoPagoError != null && mercadoPagoError.isInternalServerError()) {
            setFailureRecovery(() -> getView().startPayment());
            getView().showError(mercadoPagoError);
        } else {
            // Strange that mercadoPagoError can be nullable here, but it was like this
            getView().showError(mercadoPagoError);
        }
    }

    @Override
    public void onPaymentMethodSelectionError(final MercadoPagoError mercadoPagoError) {
        getView().cancelCheckout(mercadoPagoError);
    }

    @Override
    public void onPaymentMethodSelectionCancel() {
        cancelCheckout();
    }

    @Override
    public void onReviewAndConfirmCancel() {
        if (isUniquePaymentMethod()) {
            getView().cancelCheckout();
        } else {
            state.paymentMethodEdited = true;
            getView().showPaymentMethodSelection();
            //Back button in R&C
            getView().transitionOut();
        }
    }

    @Override
    public void onReviewAndConfirmError(final MercadoPagoError mercadoPagoError) {
        getView().cancelCheckout(mercadoPagoError);
    }

    @Override
    public void onPaymentResultResponse() {
        finishCheckout();
    }

    @Override
    public void onCardFlowResponse() {
        if (paymentRepository.hasRecoverablePayment()) {
            getView().startPayment();
        } else {
            onPaymentMethodSelected();
        }
    }

    @Override
    public void onTerminalError(@NonNull final MercadoPagoError mercadoPagoError) {
        getView().cancelCheckout(mercadoPagoError);
    }

    @Override
    public void onCardFlowCancel() {
        initRepository.init().execute(new Callback<InitResponse>() {
            @Override
            public void success(final InitResponse initResponse) {
                new DefaultPaymentMethodDriver(initResponse,
                    paymentSettingRepository.getCheckoutPreference().getPaymentPreference()).drive(
                    new DefaultPaymentMethodDriver.PaymentMethodDriverCallback() {
                        @Override
                        public void driveToCardVault(@NonNull final Card card) {
                            cancelCheckout();
                        }

                        @Override
                        public void driveToNewCardFlow(final String defaultPaymentTypeId) {
                            cancelCheckout();
                        }

                        @Override
                        public void doNothing() {
                            state.paymentMethodEdited = true;
                            getView().showPaymentMethodSelection();
                        }
                    });
            }

            @Override
            public void failure(final ApiException apiException) {
                state.paymentMethodEdited = true;
                getView().showPaymentMethodSelection();
            }
        });
    }

    @Override
    public void onCustomReviewAndConfirmResponse(final Integer customResultCode) {
        getView().cancelCheckout(customResultCode, state.paymentMethodEdited);
    }

    private void savePaymentMethodQuantity(final PaymentMethodSearch paymentMethodSearch) {
        final int pluginCount = pluginRepository.getPaymentMethodPluginCount();
        int groupCount = 0;
        int customCount = 0;

        if (paymentMethodSearch != null && paymentMethodSearch.hasSearchItems()) {
            groupCount = paymentMethodSearch.getGroups().size();
            if (pluginCount == 0 && groupCount == 1 && paymentMethodSearch.getGroups().get(0).isGroup()) {
                state.isUniquePaymentMethod = false;
            }
        }

        if (paymentMethodSearch != null && paymentMethodSearch.hasCustomSearchItems()) {
            customCount = paymentMethodSearch.getCustomSearchItems().size();
        }

        state.isUniquePaymentMethod = groupCount + customCount + pluginCount == 1;
    }

    @Override
    public void recoverFromFailure() {
        if (failureRecovery != null) {
            failureRecovery.recover();
        } else {
            getView().showFailureRecoveryError();
        }
    }

    @Override
    public void setFailureRecovery(final FailureRecovery failureRecovery) {
        this.failureRecovery = failureRecovery;
    }

    private void finishCheckout() {
        //TODO improve this
        if (paymentRepository.hasPayment() && paymentRepository.getPayment() instanceof Payment) {
            getView().finishWithPaymentResult((Payment) paymentRepository.getPayment());
        } else {
            getView().finishWithPaymentResult();
        }
    }

    @Override
    public void onCustomPaymentResultResponse(final Integer customResultCode) {
        //TODO improve this
        if (paymentRepository.hasPayment() && paymentRepository.getPayment() instanceof Payment) {
            getView().finishWithPaymentResult(customResultCode, (Payment) paymentRepository.getPayment());
        } else {
            getView().finishWithPaymentResult(customResultCode);
        }
    }

    /**
     * Send intention to close checkout if the checkout has oneTap data then it should not close.
     */
    @Override
    public void cancelCheckout() {
        //TODO improve this
        if (state.isExpressCheckout) {
            getView().hideProgress();
        } else {
            getView().cancelCheckout();
        }
    }

    private void saveIsExpressCheckout(final PaymentMethodSearch paymentMethodSearch) {
        state.isExpressCheckout = paymentMethodSearch.hasExpressCheckoutMetadata();
    }

    /**
     * Close checkout with resCode
     */
    @Override
    public void exitWithCode(final int resCode) {
        getView().exitCheckout(resCode);
    }

    @Override
    public boolean isUniquePaymentMethod() {
        return state.isUniquePaymentMethod;
    }

    @Override
    public void onCvvRequired(@NonNull final Card card, @NonNull final Reason reason) {
        getView().showSavedCardFlow(card);
    }

    @Override
    public void onVisualPayment() {
        getView().showPaymentProcessor();
    }

    @Override
    public void onPaymentFinished(@NonNull final IPaymentDescriptor payment) {
        if(!state.isExpressCheckout) {
            congratsRepository.getPostPaymentData(payment, paymentRepository.createPaymentResult(payment),
                this::handleResult);
        }
    }

    private void handleResult(@NonNull final PaymentModel paymentModel) {
        getView().hideProgress();
        paymentModel.process(new PaymentModelHandler() {
            @Override
            public void visit(@NonNull final PaymentModel paymentModel) {
                getView().showPaymentResult(paymentModel);
            }

            @Override
            public void visit(@NonNull final BusinessPaymentModel businessPaymentModel) {
                getView().showBusinessResult(businessPaymentModel);
            }
        });
    }

    @Override
    public void onPaymentError(@NonNull final MercadoPagoError error) {
        getView().hideProgress();
        recoverCreatePayment(error);
    }

    private void recoverCreatePayment(final MercadoPagoError error) {
        setFailureRecovery(() -> getView().startPayment());
        resolvePaymentFailure(error);
    }

    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        if (!state.isExpressCheckout) {
            getView().startPaymentRecoveryFlow(recovery);
        }
    }

    @Override
    public void recoverPayment(@NonNull final PostPaymentAction postPaymentAction) {
        if (!state.isExpressCheckout) {
            getView().showReviewAndConfirmAndRecoverPayment(isUniquePaymentMethod(), postPaymentAction);
        }
    }

    @Override
    public void onChangePaymentMethod() {
        state.paymentMethodEdited = true;
        userSelectionRepository.reset();
        paymentSettingRepository.clearToken();
        getView().transitionOut();

        new OnChangePaymentMethodDriver(internalConfiguration, state, paymentRepository)
            .drive(new OnChangePaymentMethodDriver.ChangePaymentMethodDriverCallback() {
                @Override
                public void driveToFinishWithPaymentResult(final Integer resultCode, final Payment payment) {
                    getView().finishWithPaymentResult(resultCode, payment);
                }

                @Override
                public void driveToFinishWithoutPaymentResult(final Integer resultCode) {
                    getView().finishWithPaymentResult(resultCode);
                }

                @Override
                public void driveToShowPaymentMethodSelection() {
                    getView().showPaymentMethodSelection();
                }
            });
    }

    //TODO separate with better navigation when we have a proper driver.
    @Override
    public void onChangePaymentMethodFromReviewAndConfirm() {
        onChangePaymentMethod();
    }

    @Override
    public void onCardAdded(@NonNull final String cardId, @NonNull final LifecycleListener.Callback callback) {
        initRepository.refreshWithNewCard(cardId).enqueue(new Callback<InitResponse>() {
            @Override
            public void success(final InitResponse initResponse) {
                callback.onSuccess();
            }

            @Override
            public void failure(final ApiException apiException) {
                callback.onError();
            }
        });
    }
}
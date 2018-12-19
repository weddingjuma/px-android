package com.mercadopago.android.px.internal.features;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpPresenter;
import com.mercadopago.android.px.internal.callbacks.FailureRecovery;
import com.mercadopago.android.px.internal.callbacks.TaggedCallback;
import com.mercadopago.android.px.internal.features.providers.SecurityCodeProvider;
import com.mercadopago.android.px.internal.features.uicontrollers.card.CardView;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.SavedCardToken;
import com.mercadopago.android.px.model.SavedESCCardToken;
import com.mercadopago.android.px.model.Setting;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.CardTokenException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker;
import com.mercadopago.android.px.tracking.internal.views.CvvAskViewTracker;

public class SecurityCodePresenter extends MvpPresenter<SecurityCodeActivityView, SecurityCodeProvider> {

    @NonNull private final PaymentSettingRepository paymentSettingRepository;
    private FailureRecovery mFailureRecovery;

    //Card Info
    private int mSecurityCodeLength;
    private String mSecurityCodeLocation;
    private int mCardNumberLength;
    private String mSecurityCode;

    //Activity parameters
    private PaymentMethod mPaymentMethod;
    private CardInfo mCardInfo;
    private Card mCard;
    private Token mToken;
    private PaymentRecovery mPaymentRecovery;

    public SecurityCodePresenter(@NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.paymentSettingRepository = paymentSettingRepository;
    }

    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public void setToken(final Token token) {
        mToken = token;
    }

    public void setCard(final Card card) {
        mCard = card;
    }

    public void setCardInfo(final CardInfo cardInfo) {
        mCardInfo = cardInfo;
    }

    public void setPaymentRecovery(final PaymentRecovery paymentRecovery) {
        mPaymentRecovery = paymentRecovery;
    }

    public PaymentRecovery getPaymentRecovery() {
        return mPaymentRecovery;
    }

    public CardInfo getCardInfo() {
        return mCardInfo;
    }

    private void setFailureRecovery(final FailureRecovery failureRecovery) {
        mFailureRecovery = failureRecovery;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public Token getToken() {
        return mToken;
    }

    public Card getCard() {
        return mCard;
    }

    public int getSecurityCodeLength() {
        return mSecurityCodeLength;
    }

    public String getSecurityCodeLocation() {
        return mSecurityCodeLocation;
    }

    public int getCardNumberLength() {
        return mCardNumberLength;
    }

    public void validate() throws IllegalStateException {
        if (mToken == null && mCard == null) {
            throw new IllegalStateException(getResourcesProvider().getTokenAndCardNotSetMessage());
        }

        if (mToken != null && mCard != null && mPaymentRecovery == null) {
            throw new IllegalStateException(
                getResourcesProvider().getTokenAndCardWithoutRecoveryCantBeBothSetMessage());
        }

        if (mPaymentMethod == null) {
            throw new IllegalStateException(getResourcesProvider().getPaymentMethodNotSetMessage());
        }

        if (mCardInfo == null) {
            throw new IllegalStateException(getResourcesProvider().getCardInfoNotSetMessage());
        }
    }

    public void initialize() {
        try {
            validate();
            getView().initialize();
            getView().showTimer();
            new CvvAskViewTracker(mCard, getPaymentMethod().getPaymentTypeId()).track();
        } catch (final IllegalStateException exception) {
            final String standardErrorMessage = getResourcesProvider().getStandardErrorMessageGotten();
            getView().showError(new MercadoPagoError(standardErrorMessage, false), "");
        }
    }

    public void recoverFromFailure() {
        if (mFailureRecovery != null) {
            mFailureRecovery.recover();
        }
    }

    public void initializeSettings() {
        if (mCardInfo != null) {
            final Setting setting =
                Setting.getSettingByPaymentMethodAndBin(mPaymentMethod, mCardInfo.getFirstSixDigits());
            initializeSecurityCodeSettings(setting);
            initializeCardNumberSettings(setting);
            getView().setSecurityCodeInputMaxLength(mSecurityCodeLength);
        }
    }

    private boolean securityCodeSettingsAvailable() {
        return mCardInfo != null && mCardInfo.getSecurityCodeLength() != null &&
            mCardInfo.getSecurityCodeLocation() != null;
    }

    private void initializeSecurityCodeSettings(final Setting setting) {
        if (securityCodeSettingsAvailable()) {
            mSecurityCodeLength = mCardInfo.getSecurityCodeLength();
            mSecurityCodeLocation = mCardInfo.getSecurityCodeLocation();
        } else if (setting != null && setting.getSecurityCode() != null) {
            mSecurityCodeLength = setting.getSecurityCode().getLength();
            mSecurityCodeLocation = setting.getSecurityCode().getCardLocation();
        } else {
            mSecurityCodeLength = Card.CARD_DEFAULT_SECURITY_CODE_LENGTH;
            mSecurityCodeLocation = CardView.CARD_SIDE_BACK;
        }
    }

    private void initializeCardNumberSettings(final Setting setting) {
        if (setting != null && setting.getCardNumber() != null) {
            mCardNumberLength = setting.getCardNumber().getLength();
        } else {
            mCardNumberLength = Card.CARD_NUMBER_MAX_LENGTH;
        }
    }

    public void saveSecurityCode(final String securityCode) {
        mSecurityCode = securityCode;
    }

    public void validateSecurityCodeInput() {
        try {
            if (hasToCloneToken() && validateSecurityCodeFromToken()) {
                cloneToken();
            } else if (isSavedCardWithESC() || hasToRecoverTokenFromESC()) {
                createTokenWithESC();
            } else if (isSavedCardWithoutESC()) {
                createTokenWithoutESC();
            }
        } catch (final CardTokenException exception) {

            FrictionEventTracker.with(FrictionEventTracker.Id.INVALID_CVV,
                new CvvAskViewTracker(getCard(), getPaymentMethod().getPaymentTypeId()),
                FrictionEventTracker.Style.CUSTOM_COMPONENT,
                getPaymentMethod()).track();

            getView().setErrorView(exception);
        }
    }

    private void createTokenWithESC() throws CardTokenException {
        final SavedESCCardToken savedESCCardToken;
        if (mCard != null) {
            savedESCCardToken = SavedESCCardToken.createWithSecurityCode(mCard.getId(), mSecurityCode);
            getResourcesProvider().validateSecurityCodeFromToken(savedESCCardToken, mCard);
            createESCToken(savedESCCardToken);
        } else if (mToken != null) {
            savedESCCardToken = SavedESCCardToken.createWithSecurityCode(mToken.getCardId(), mSecurityCode);
            validateSecurityCodeFromToken();
            createESCToken(savedESCCardToken);
        }
    }

    private void createTokenWithoutESC() throws CardTokenException {
        SavedCardToken savedCardToken = new SavedCardToken(mCard.getId(), mSecurityCode);
        getResourcesProvider().validateSecurityCodeFromToken(savedCardToken, mCard);
        createToken(savedCardToken);
    }

    private boolean hasToCloneToken() {
        return mPaymentRecovery != null && (mPaymentRecovery.isStatusDetailCallForAuthorize() ||
            mPaymentRecovery.isStatusDetailCardDisabled()) && mToken != null;
    }

    private boolean hasToRecoverTokenFromESC() {
        return mPaymentRecovery != null && mPaymentRecovery.isStatusDetailInvalidESC() &&
            ((mToken != null && mToken.getCardId() != null && !mToken.getCardId().isEmpty()) ||
                (mCard != null && mCard.getId() != null && !mCard.getId().isEmpty()));
    }

    private boolean isSavedCardWithESC() {
        return mCard != null && getResourcesProvider().isESCEnabled();
    }

    private boolean isSavedCardWithoutESC() {
        return mCard != null && !getResourcesProvider().isESCEnabled();
    }

    private boolean validateSecurityCodeFromToken() {
        try {
            if (!TextUtil.isEmpty(mToken.getFirstSixDigits())) {
                getResourcesProvider()
                    .validateSecurityCodeFromToken(mSecurityCode, mPaymentMethod, mToken.getFirstSixDigits());
            } else {
                getResourcesProvider().validateSecurityCodeFromToken(mSecurityCode);
            }
            getView().clearErrorView();
            return true;
        } catch (final CardTokenException exception) {
            getView().setErrorView(exception);
            return false;
        }
    }

    private void cloneToken() {
        getView().showLoadingView();

        getResourcesProvider()
            .cloneToken(mToken.getId(), new TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                @Override
                public void onSuccess(final Token token) {
                    mToken = token;
                    paymentSettingRepository.configure(mToken);
                    MPTracker.getInstance().trackTokenId(mToken.getId(), paymentSettingRepository.getPublicKey(),
                        paymentSettingRepository.getCheckoutPreference().getSite());
                    putSecurityCode();
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    if (isViewAttached()) {
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                cloneToken();
                            }
                        });
                        getView().stopLoadingView();
                        getView().showError(error, ApiUtil.RequestOrigin.CREATE_TOKEN);
                    }
                }
            });
    }

    public void putSecurityCode() {

        getResourcesProvider().putSecurityCode(mSecurityCode, mToken.getId(),
            new TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                @Override
                public void onSuccess(final Token token) {
                    resolveTokenCreation(token);
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    if (isViewAttached()) {
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                cloneToken();
                            }
                        });
                        getView().stopLoadingView();
                        getView().showError(error, ApiUtil.RequestOrigin.CREATE_TOKEN);
                    }
                }
            });
    }

    private void createToken(final SavedCardToken savedCardToken) {
        getView().showLoadingView();

        getResourcesProvider()
            .createToken(savedCardToken, new TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                @Override
                public void onSuccess(Token token) {
                    resolveTokenCreation(token);
                }

                @Override
                public void onFailure(MercadoPagoError error) {
                    if (isViewAttached()) {
                        setFailureRecovery(new FailureRecovery() {
                            @Override
                            public void recover() {
                                createToken(savedCardToken);
                            }
                        });
                        getView().stopLoadingView();
                        getView().showError(error, ApiUtil.RequestOrigin.CREATE_TOKEN);
                    }
                }
            });
    }

    private void createESCToken(final SavedESCCardToken savedESCCardToken) {
        getView().showLoadingView();

        getResourcesProvider()
            .createToken(savedESCCardToken, new TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                @Override
                public void onSuccess(final Token token) {
                    resolveTokenCreation(token);
                }

                @Override
                public void onFailure(final MercadoPagoError error) {
                    setFailureRecovery(new FailureRecovery() {
                        @Override
                        public void recover() {
                            createESCToken(savedESCCardToken);
                        }
                    });
                    getView().stopLoadingView();
                    getView().showError(error, ApiUtil.RequestOrigin.CREATE_TOKEN);
                }
            });
    }

    private void resolveTokenCreation(final Token token) {
        mToken = token;
        if (mCardInfo != null) {
            mToken.setLastFourDigits(mCardInfo.getLastFourDigits());
        }
        paymentSettingRepository.configure(mToken);
        getView().finishWithResult();
    }

    public void setSecurityCodeCardType() {
        if (getSecurityCodeLocation().equals(CardView.CARD_SIDE_BACK)) {
            getView().showBackSecurityCodeCardView();
        } else {
            getView().showFrontSecurityCodeCardView();
        }
    }
}
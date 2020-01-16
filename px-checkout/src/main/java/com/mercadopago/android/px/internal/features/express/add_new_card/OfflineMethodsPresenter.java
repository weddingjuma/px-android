package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.addons.model.SecurityValidationData;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.core.ProductIdProvider;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.util.SecurityValidationDataFactory;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel;
import com.mercadopago.android.px.internal.viewmodel.mappers.PayButtonViewModelMapper;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.model.exceptions.NoConnectivityException;
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker;
import com.mercadopago.android.px.tracking.internal.views.OneTapViewTracker;

class OfflineMethodsPresenter extends BasePresenter<OfflineMethods.OffMethodsView> implements OfflineMethods.Actions {

    @NonNull private final PaymentRepository paymentRepository;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;
    @NonNull private final ExplodeDecoratorMapper explodeDecoratorMapper;
    @NonNull private final PayButtonViewModel payButtonViewModel;
    @NonNull private final AmountRepository amountRepository;
    @NonNull private final DiscountRepository discountRepository;
    private ProductIdProvider productIdProvider;
    @NonNull private final String defaultPaymentTypeId;

    private OfflineMethodItem selectedItem;

    /* default */ OfflineMethodsPresenter(@NonNull final PaymentRepository paymentRepository,
        @NonNull final PaymentSettingRepository paymentSettingRepository,
        @NonNull final AmountRepository amountRepository,
        @NonNull final DiscountRepository discountRepository,
        @NonNull final ProductIdProvider productIdProvider,
        @NonNull final String defaultPaymentTypeId) {
        this.paymentRepository = paymentRepository;
        this.paymentSettingRepository = paymentSettingRepository;
        this.amountRepository = amountRepository;
        this.discountRepository = discountRepository;
        this.productIdProvider = productIdProvider;
        this.defaultPaymentTypeId = defaultPaymentTypeId;

        payButtonViewModel = new PayButtonViewModelMapper().map(
            paymentSettingRepository.getAdvancedConfiguration().getCustomStringConfiguration());

        explodeDecoratorMapper = new ExplodeDecoratorMapper();
    }

    @Override
    public void detachView() {
        onViewPaused();
        super.detachView();
    }

    @Override
    public void onViewResumed() {
        paymentRepository.attach(this);
    }

    @Override
    public void onViewPaused() {
        paymentRepository.detach(this);
    }

    @Override
    public void loadViewModel() {
        updateModel();
    }

    private void updateModel() {
        final String paymentTypeId =
            selectedItem != null ? selectedItem.getPaymentTypeId() : defaultPaymentTypeId;

        final AmountLocalized amountLocalized = new AmountLocalized(
            amountRepository.getAmountToPay(paymentTypeId, discountRepository.getCurrentConfiguration()),
            paymentSettingRepository.getCurrency());

        getView().updateTotalView(amountLocalized);
    }

    @Override
    public void selectMethod(@NonNull final OfflineMethodItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void startSecuredPayment() {
        final SecurityValidationData data = SecurityValidationDataFactory.create(productIdProvider);
        getView().startSecurityValidation(data);
    }

    @Override
    public void startPayment() {
        // TODO add securityValidation
        refreshExplodingState();

        //noinspection ConstantConditions
        paymentRepository
            .startExpressPaymentWithOffMethod(selectedItem.getPaymentMethodId(), selectedItem.getPaymentTypeId());
    }

    @Override
    public void trackSecurityFriction() {
        // TODO Review ID
        FrictionEventTracker
            .with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW, FrictionEventTracker.Id.GENERIC,
                FrictionEventTracker.Style.CUSTOM_COMPONENT).track();
    }

    private void refreshExplodingState() {
        if (paymentRepository.isExplodingAnimationCompatible()) {
            getView().startLoadingButton(paymentRepository.getPaymentTimeout(), payButtonViewModel);
            getView().disableCloseButton();
        }
    }

    public void hasFinishPaymentAnimation() {
        final IPaymentDescriptor payment = paymentRepository.getPayment();
        if (payment != null) {
            getView().showPaymentResult(payment);
        }
    }

    @Override
    public void onPaymentFinished(@NonNull final IPaymentDescriptor payment) {
        getView().finishLoading(explodeDecoratorMapper.map(payment));
    }

    @Override
    public void onPaymentError(@NonNull final MercadoPagoError error) {
        getView().cancelLoading();
        if (error.isInternalServerError() || error.isNoConnectivityError()) {
            FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
                FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT,
                error)
                .track();
            getView().showErrorSnackBar(error);
        } else {
            getView().showErrorScreen(error);
        }
    }

    @Override
    public void onCvvRequired(@NonNull final Card card) {
        // do nothing
    }

    @Override
    public void onVisualPayment() {
        getView().showPaymentProcessor();
    }

    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        // do nothing
    }

    public void manageNoConnection() {
        final NoConnectivityException exception = new NoConnectivityException();
        final ApiException apiException = ApiUtil.getApiException(exception);
        final MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, null);
        FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
            FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT,
            mercadoPagoError)
            .track();
        getView().showErrorSnackBar(mercadoPagoError);
    }
}

package com.mercadopago.android.px.internal.features.express;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpPresenter;
import com.mercadopago.android.px.internal.base.ResourcesProvider;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.tracker.Tracker;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.NoConnectivityException;
import com.mercadopago.android.px.internal.view.AmountDescriptorView;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.internal.view.SummaryDetailDescriptorFactory;
import com.mercadopago.android.px.internal.view.SummaryView;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.EmptyInstallmentsDescriptor;
import com.mercadopago.android.px.internal.viewmodel.InstallmentsDescriptorNoPayerCost;
import com.mercadopago.android.px.internal.viewmodel.InstallmentsDescriptorWithPayerCost;
import com.mercadopago.android.px.internal.viewmodel.TotalDetailColor;
import com.mercadopago.android.px.internal.viewmodel.TotalLocalized;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import com.mercadopago.android.px.internal.viewmodel.mappers.PaymentMethodDrawableItemMapper;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.services.Callback;
import java.util.ArrayList;
import java.util.List;

import static com.mercadopago.android.px.internal.view.InstallmentsDescriptorView.Model.SELECTED_PAYER_COST_NONE;

/* default */ class ExpressPaymentPresenter extends MvpPresenter<ExpressPayment.View, ResourcesProvider>
    implements ExpressPayment.Actions {

    @NonNull private final PaymentRepository paymentRepository;
    @NonNull private final AmountRepository amountRepository;
    @NonNull private final DiscountRepository discountRepository;
    @NonNull private final PaymentSettingRepository configuration;
    @NonNull private final ExplodeDecoratorMapper explodeDecoratorMapper;
    @NonNull private final ElementDescriptorMapper elementDescriptorMapper;

    private int userSelectedPayerCost = SELECTED_PAYER_COST_NONE;

    //TODO remove.
    private List<ExpressMetadata> expressMetadataList;
    private PaymentMethodDrawableItemMapper paymentMethodDrawableItemMapper;

    /* default */ ExpressPaymentPresenter(@NonNull final PaymentRepository paymentRepository,
        @NonNull final PaymentSettingRepository configuration,
        @NonNull final DiscountRepository discountRepository,
        @NonNull final AmountRepository amountRepository,
        @NonNull final ElementDescriptorMapper elementDescriptorMapper,
        @NonNull final GroupsRepository groupsRepository) {
        this.paymentRepository = paymentRepository;
        this.configuration = configuration;
        this.amountRepository = amountRepository;
        this.discountRepository = discountRepository;
        explodeDecoratorMapper = new ExplodeDecoratorMapper();
        this.elementDescriptorMapper = elementDescriptorMapper;
        paymentMethodDrawableItemMapper = new PaymentMethodDrawableItemMapper();

        groupsRepository.getGroups().execute(new Callback<PaymentMethodSearch>() {
            @Override
            public void success(final PaymentMethodSearch paymentMethodSearch) {
                expressMetadataList = paymentMethodSearch.getExpress();
            }

            @Override
            public void failure(final ApiException apiException) {
                throw new IllegalStateException("groups missing rendering one tap");
            }
        });
    }

    @Override
    public void trackConfirmButton(final int paymentMethodSelectedIndex) {
        //Track event: confirm one tap
        final ExpressMetadata expressMetadata = expressMetadataList.get(paymentMethodSelectedIndex);
        Tracker.trackConfirmExpress(expressMetadata, userSelectedPayerCost,
            configuration.getCheckoutPreference().getSite().getCurrencyId());
    }

    @Override
    public void trackExpressView() {
            Tracker.trackExpressView(amountRepository.getAmountToPay(),
                configuration.getCheckoutPreference().getSite().getCurrencyId(), discountRepository.getDiscount(),
                discountRepository.getCampaign(), configuration.getCheckoutPreference().getItems(),
                expressMetadataList);
    }

    @Override
    public void confirmPayment(final int paymentMethodSelectedIndex) {
        if (paymentRepository.isExplodingAnimationCompatible()) {
            getView().startLoadingButton(paymentRepository.getPaymentTimeout());
            getView().hideConfirmButton();
            getView().disableToolbarBack();
        }

        // TODO improve: This was added because onetap can detach this listener on its OnDestroy
        paymentRepository.attach(this);

        final ExpressMetadata expressMetadata = expressMetadataList.get(paymentMethodSelectedIndex);
        //TODO fix installment selected.
        final PayerCost payerCost =
            expressMetadata.isCard() ? expressMetadata.getCard().getPayerCost(userSelectedPayerCost) : null;

        paymentRepository.startExpressPayment(expressMetadata, payerCost);
    }

    @Override
    public void cancel() {
        getView().cancel();
    }

    //TODO verify if current item still persist when activity is destroyed.
    @Override
    public void onTokenResolved(final int paymentMethodSelectedIndex) {
        cancelLoading();
        confirmPayment(paymentMethodSelectedIndex);
    }

    @Override
    public void onPaymentFinished(@NonNull final Payment payment) {
        getView().finishLoading(explodeDecoratorMapper.map(payment));
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param genericPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final GenericPayment genericPayment) {
        getView().finishLoading(explodeDecoratorMapper.map(genericPayment));
    }

    /**
     * When there is no visual interaction needed this callback is called.
     *
     * @param businessPayment plugin payment.
     */
    @Override
    public void onPaymentFinished(@NonNull final BusinessPayment businessPayment) {
        getView().finishLoading(explodeDecoratorMapper.map(businessPayment));
    }

    @Override
    public void onPaymentError(@NonNull final MercadoPagoError error) {
        cancelLoading();

        if (error.isInternalServerError() || error.isNoConnectivityError()) {
            getView().showErrorSnackBar(error);
        } else {
            getView().showErrorScreen(error);
        }
    }

    @Override
    public void onVisualPayment() {
        getView().showPaymentProcessor();
    }

    @Override
    public void onCvvRequired(@NonNull final Card card) {
        cancelLoading();
        getView().showCardFlow(card);
    }

    @Override
    public void onRecoverPaymentEscInvalid(final PaymentRecovery recovery) {
        getView().onRecoverPaymentEscInvalid(recovery);
    }

    @Override
    public void onViewResumed() {
        // If a payment was attempted, the exploding fragment is still visible when we go back to one tap fragment.
        // Example: call for authorize, after asking for cvv and pressing back, we go back to one tap and need to
        // remove the exploding fragment we had before.

        if (paymentRepository.hasPayment()) {
            cancelLoading();
        }
        paymentRepository.attach(this);
    }

    @Override
    public void updateElementPosition(final int paymentMethodIndex) {
        getView().hideInstallmentsSelection();
        getView().showInstallmentsDescriptionRow(paymentMethodIndex, userSelectedPayerCost);

        if (isLastElement(paymentMethodIndex)) {
            getView().disablePaymentButton();
        } else {
            getView().enablePaymentButton();
        }
    }

    private void updateElementPosition(final int paymentMethodIndex, final int selectedPayerCost) {
        userSelectedPayerCost = selectedPayerCost;
        updateElementPosition(paymentMethodIndex);
    }

    private InstallmentsDescriptorView.Model createInstallmentsDescriptorModel(final int paymentMethodIndex) {

        if (isLastElement(paymentMethodIndex)) {
            //Last card is Add new payment method card
            return EmptyInstallmentsDescriptor.create();
        } else {
            final ExpressMetadata expressMetadata = expressMetadataList.get(paymentMethodIndex);
            final String paymentTypeId = expressMetadata.getPaymentTypeId();

            final CardMetadata cardMetadata = expressMetadata.getCard();

            if (PaymentTypes.isCreditCardPaymentType(paymentTypeId)) {
                //This model is useful for Credit Card only

                return InstallmentsDescriptorWithPayerCost.createFrom(configuration, cardMetadata,
                    userSelectedPayerCost == SELECTED_PAYER_COST_NONE ? cardMetadata.selectedPayerCostIndex
                        : userSelectedPayerCost);
            } else if (!expressMetadata.isCard() || PaymentTypes.DEBIT_CARD.equals(paymentTypeId) ||
                PaymentTypes.PREPAID_CARD.equals(paymentTypeId)) {
                //This model is useful in case of One payment method (account money or debit) to represent an empty row
                return EmptyInstallmentsDescriptor.create();
            } else {
                //This model is useful in case of Two payment methods (account money and debit) to represent the Debit row
                return InstallmentsDescriptorNoPayerCost.createFrom(configuration, cardMetadata);
            }
        }
    }

    private boolean isLastElement(final int position) {
        return position >= expressMetadataList.size();
    }

    @Override
    public void attachView(final ExpressPayment.View view) {
        super.attachView(view);

        final List<AmountDescriptorView.Model> summaryDetailList =
            new SummaryDetailDescriptorFactory(discountRepository, configuration).create();

        final AmountDescriptorView.Model totalRow = new AmountDescriptorView.Model(
            new TotalLocalized(),
            new AmountLocalized(amountRepository.getAmountWithDiscount(),
                configuration.getCheckoutPreference().getSite().getCurrencyId()),
            new TotalDetailColor());

        final ElementDescriptorView.Model elementDescriptorModel =
            elementDescriptorMapper.map(configuration.getCheckoutPreference());

        final SummaryView.Model summaryModel =
            new SummaryView.Model(elementDescriptorModel,
                summaryDetailList, totalRow);

        getView().updateSummary(summaryModel);
        getView().showToolbarElementDescriptor(elementDescriptorModel);

        getView().configurePagerAndInstallments(paymentMethodDrawableItemMapper.map(expressMetadataList),
            configuration.getCheckoutPreference().getSite(), SELECTED_PAYER_COST_NONE,
            getInstallmentsModel());
    }

    private List<InstallmentsDescriptorView.Model> getInstallmentsModel() {
        final List<InstallmentsDescriptorView.Model> models = new ArrayList<>();

        for (int index = 0; index <= expressMetadataList.size(); index++) {
            models.add(createInstallmentsDescriptorModel(index));
        }

        return models;
    }

    @Override
    public void onViewPaused() {
        paymentRepository.detach(this);
    }

    @Override
    public void onInstallmentsRowPressed(final int currentItem) {
        final CardMetadata cardMetadata = expressMetadataList.get(currentItem).getCard();
        if (currentItem <= expressMetadataList.size() && cardMetadata != null) {
            final List<PayerCost> payerCostList = cardMetadata.payerCosts;
            if (payerCostList != null && !payerCostList.isEmpty()) {
                getView().showInstallmentsList(payerCostList,
                    userSelectedPayerCost == SELECTED_PAYER_COST_NONE ? cardMetadata.selectedPayerCostIndex
                        : userSelectedPayerCost);
                trackInstallments(expressMetadataList.get(currentItem));
            }
        }
    }

    public void trackInstallments(@NonNull final ExpressMetadata expressMetadata) {
        Tracker.trackExpressInstallmentsView(expressMetadata,
            configuration.getCheckoutPreference().getSite().getCurrencyId(), amountRepository.getAmountToPay());
    }

    /**
     * When user cancel the payer cost selection this method
     * will be called with the current payment method position
     *
     * @param position current payment method position.
     */
    @Override
    public void onInstallmentSelectionCanceled(final int position) {
        updateElementPosition(position);
        getView().collapseInstallmentsSelection();
    }

    /**
     * When user selects a new payment method
     * this method will be called with the new current paymentMethodIndex.
     *
     * @param paymentMethodIndex current payment method paymentMethodIndex.
     */
    @Override
    public void onSliderOptionSelected(final int paymentMethodIndex) {
        updateElementPosition(paymentMethodIndex, SELECTED_PAYER_COST_NONE);
    }

    /**
     * When user selects a new payer cost for certain payment method
     * this method will be called.
     *
     * @param paymentMethodIndex current payment method position.
     * @param payerCostSelected user selected payerCost.
     */
    @Override
    public void onPayerCostSelected(final int paymentMethodIndex, final PayerCost payerCostSelected) {
        final CardMetadata cardMetadata = expressMetadataList.get(paymentMethodIndex).getCard();
        final int selected = cardMetadata.payerCosts.indexOf(payerCostSelected);
        updateElementPosition(paymentMethodIndex, selected);
        getView().collapseInstallmentsSelection();
    }

    @Override
    public void detachView() {
        onViewPaused();
        super.detachView();
    }

    // Keep - Save state
    public int getSelected() {
        return userSelectedPayerCost;
    }

    // Keep - Restored state
    public void setSelectedPayerCost(final int selectedPayerCost) {
        userSelectedPayerCost = selectedPayerCost;
    }

    @Override
    public void hasFinishPaymentAnimation() {
        final IPayment payment = paymentRepository.getPayment();
        if (payment != null) {
            getView().showPaymentResult(payment);
        }
    }

    private void cancelLoading() {
        getView().enableToolbarBack();
        getView().showConfirmButton();
        getView().cancelLoading();
    }

    @Override
    public void manageNoConnection() {
        final NoConnectivityException exception = new NoConnectivityException();
        final ApiException apiException = ApiUtil.getApiException(exception);
        final MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, null);
        getView().showErrorSnackBar(mercadoPagoError);
    }
}
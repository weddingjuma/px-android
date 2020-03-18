package com.mercadopago.android.px.internal.features.pay_button

import android.arch.lifecycle.MutableLiveData
import com.mercadopago.android.px.addons.model.SecurityValidationData
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.core.ConnectionHelper
import com.mercadopago.android.px.internal.core.ProductIdProvider
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.util.ApiUtil
import com.mercadopago.android.px.internal.util.SecurityValidationDataFactory
import com.mercadopago.android.px.internal.viewmodel.mappers.PayButtonViewModelMapper
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.exceptions.NoConnectivityException
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.events.ConfirmEvent
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker
import com.mercadopago.android.px.tracking.internal.model.ConfirmData
import com.mercadopago.android.px.tracking.internal.model.Reason
import com.mercadopago.android.px.tracking.internal.views.OneTapViewTracker
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as ButtonConfig

internal class PayButtonViewModel(
    private var paymentService: PaymentRepository,
    private var productIdProvider: ProductIdProvider,
    private var connectionHelper: ConnectionHelper,
    paymentSettingRepository: PaymentSettingRepository) : BaseViewModel(), PayButton.ViewModel {

    private var confirmTrackerData: ConfirmData? = null
    private var paymentConfiguration: PaymentConfiguration? = null

    val buttonTextLiveData = MutableLiveData<ButtonConfig>()
    val paymentStartedLiveData = MutableLiveData<Pair<Int, ButtonConfig>>()
    val cvvRequiredLiveData = MutableLiveData<Pair<Card, Reason>>()
    val recoverRequiredLiveData = MutableLiveData<PaymentRecovery>()
    val stateUILiveData = MutableLiveData<PayButtonState>()

    private var buttonConfig: ButtonConfig = PayButtonViewModelMapper().map(
        paymentSettingRepository.advancedConfiguration.customStringConfiguration)

    init {
        buttonTextLiveData.postValue(buttonConfig)
    }

    private lateinit var handler: PayButton.Handler

    override fun attach(handler: PayButton.Handler) {
        this.handler = handler
    }

    override fun startSecuredPayment(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData) {
        this.paymentConfiguration = paymentConfiguration
        this.confirmTrackerData = confirmTrackerData
        val data: SecurityValidationData = SecurityValidationDataFactory
            .create(productIdProvider, paymentConfiguration)
        stateUILiveData.postValue(UIProgress.FingerprintRequired(data))
    }

    override fun handleBiometricsResult(isSuccess: Boolean) {
        if (isSuccess) {
            startPayment()
        } else {
            FrictionEventTracker
                .with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW, FrictionEventTracker.Id.GENERIC,
                    FrictionEventTracker.Style.CUSTOM_COMPONENT).track()
        }
    }

    override fun startPayment() {
        if (paymentService.isExplodingAnimationCompatible) {
            paymentStartedLiveData.postValue(Pair(paymentService.paymentTimeout, buttonConfig))
        }
        paymentService.attach(this)
        paymentService.startExpressPayment(paymentConfiguration!!)

        ConfirmEvent(confirmTrackerData!!).track()
    }

    override fun onPaymentError(error: MercadoPagoError) {
        stateUILiveData.postValue(UIProgress.ButtonLoadingCanceled)
        val shouldHandleError = error.isInternalServerError || error.isNoConnectivityError
        if (shouldHandleError) handleError(error) else handler.onPaymentError(error)
    }

    override fun onVisualPayment() {
        stateUILiveData.postValue(UIResult.VisualProcessorResult)
    }

    override fun onCvvRequired(card: Card, reason: Reason) {
        cvvRequiredLiveData.postValue(Pair(card, reason))
    }

    override fun onPaymentFinished(payment: IPaymentDescriptor) {
        stateUILiveData.postValue(UIProgress.ButtonLoadingFinished(ExplodeDecoratorMapper().map(payment)))
    }

    override fun onRecoverPaymentEscInvalid(recovery: PaymentRecovery) {
        recoverPayment(recovery)
    }

    override fun recoverPayment() {
        recoverPayment(paymentService.createPaymentRecovery())
    }

    override fun recoverPayment(recovery: PaymentRecovery) {
        if (recovery.shouldAskForCvv()) {
            recoverRequiredLiveData.postValue(recovery)
        }
    }

    override fun preparePayment() {
        paymentConfiguration = null
        confirmTrackerData = null
        if (connectionHelper.checkConnection()) handler.prePayment() else manageNoConnection()
    }

    private fun manageNoConnection() {
        val exception = NoConnectivityException()
        val apiException = ApiUtil.getApiException(exception)
        val error = MercadoPagoError(apiException, null)
        handleError(error)
    }

    private fun handleError(error: MercadoPagoError) {
        FrictionEventTracker.with(OneTapViewTracker.PATH_REVIEW_ONE_TAP_VIEW,
            FrictionEventTracker.Id.GENERIC, FrictionEventTracker.Style.CUSTOM_COMPONENT, error).track()
        stateUILiveData.postValue(UIError.ConnectionError(error))
    }

    override fun hasFinishPaymentAnimation() {
        paymentService.payment?.let { handler.onPaymentFinished(it) }
    }
}
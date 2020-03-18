package com.mercadopago.android.px.internal.features.pay_button

import android.arch.lifecycle.MutableLiveData
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator
import com.mercadopago.android.px.internal.features.explode.ExplodeDecoratorMapper
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.viewmodel.mappers.PayButtonViewModelMapper
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.Reason
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as ButtonConfig

internal class PayButtonViewModel(var paymentService: PaymentRepository, var paymentSettingRepository: PaymentSettingRepository) : BaseViewModel(), PayButton.ViewModel {

    val buttonTextLiveData = MutableLiveData<ButtonConfig>()
    val paymentStartedLiveData = MutableLiveData<Pair<Int, ButtonConfig>>()
    val finishLoadingLiveData = MutableLiveData<ExplodeDecorator>()
    val cvvRequiredLiveData = MutableLiveData<Pair<Card, Reason>>()
    val recoverRequiredLiveData = MutableLiveData<PaymentRecovery>()

    var buttonConfig: ButtonConfig = PayButtonViewModelMapper().map(
        paymentSettingRepository.advancedConfiguration.customStringConfiguration)

    init {
        buttonTextLiveData.postValue(buttonConfig)
    }

    private lateinit var handler: PayButtonFragment.PayButtonHandler

    override fun attach(handler: PayButtonFragment.PayButtonHandler) {
        this.handler = handler
    }

    override fun requireConfiguration() {
        handler.requireConfiguration()
    }

    override fun startPayment(paymentConfiguration: PaymentConfiguration) {
        if (paymentService.isExplodingAnimationCompatible) {
            handler.onLoadingStarted()
            paymentStartedLiveData.postValue(Pair(paymentService.paymentTimeout, buttonConfig))
        }
        paymentService.attach(this)
        paymentService.startExpressPayment(paymentConfiguration)
    }

    override fun onPaymentError(error: MercadoPagoError) {
        handler.onPaymentError(error)
    }

    override fun onVisualPayment() {
        // startear activity de procesadora de pago
    }

    override fun onCvvRequired(card: Card, reason: Reason) {
        cvvRequiredLiveData.postValue(Pair(card, reason))
    }

    override fun onPaymentFinished(payment: IPaymentDescriptor) {
        finishLoadingLiveData.postValue(ExplodeDecoratorMapper().map(payment))
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

    override fun prePayment() {
        handler.prePayment()
    }

    override fun onLoadingCanceled() {
        handler.onLoadingCanceled()
    }

    override fun hasFinishPaymentAnimation() {
        paymentService.payment?.let { handler.onPaymentFinished(it) }
    }
}
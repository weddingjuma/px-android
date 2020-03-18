package com.mercadopago.android.px.internal.features.pay_button

import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.internal.PaymentConfiguration

interface PayButton {

    interface View : ExplodingFragment.ExplodingAnimationListener {
        fun onConfigurationProvided(paymentConfiguration: PaymentConfiguration)
        fun onReadyForPayment()
        fun handlePaymentRecovery(paymentRecovery: PaymentRecovery)
    }

    interface ViewModel : PaymentServiceHandler {
        fun attach(handler: PayButtonFragment.PayButtonHandler)
        fun startPayment(paymentConfiguration: PaymentConfiguration)
        fun requireConfiguration()
        fun prePayment()
        fun onLoadingCanceled()
        fun hasFinishPaymentAnimation()
        fun recoverPayment()
        fun recoverPayment(recovery: PaymentRecovery)
    }
}
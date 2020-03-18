package com.mercadopago.android.px.internal.features.pay_button

import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.ConfirmData

interface PayButton {

    interface View : ExplodingFragment.ExplodingAnimationListener {
        fun handlePaymentRecovery(paymentRecovery: PaymentRecovery)
        fun onReadyForPayment(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData)
        fun isExploding(): Boolean
        fun stimulate()
    }

    interface ViewModel : PaymentServiceHandler {
        fun attach(handler: Handler)
        fun startSecuredPayment(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData)
        fun handleBiometricsResult(isSuccess: Boolean)
        fun preparePayment()
        fun hasFinishPaymentAnimation()
        fun recoverPayment()
        fun recoverPayment(recovery: PaymentRecovery)
        fun startPayment()
    }

    interface Handler {
        fun onPaymentFinished(payment: IPaymentDescriptor)
        fun onPaymentError(error: MercadoPagoError)
        fun prePayment()
    }
}
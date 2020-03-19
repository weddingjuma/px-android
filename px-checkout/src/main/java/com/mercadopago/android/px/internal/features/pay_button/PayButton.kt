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
        fun isExploding(): Boolean
        fun stimulate()
        fun enable()
        fun disable()
    }

    interface ViewModel : PaymentServiceHandler {
        fun attach(handler: Handler)
        fun preparePayment()
        fun handleBiometricsResult(isSuccess: Boolean)
        fun startPayment()
        fun hasFinishPaymentAnimation()
        fun recoverPayment()
        fun recoverPayment(recovery: PaymentRecovery)
    }

    interface Handler {
        fun prePayment(callback: OnReadyForPaymentCallback)
        @JvmDefault fun enqueueOnExploding(callback: OnEnqueueResolvedCallback) { callback.success() }
        fun onPaymentFinished(payment: IPaymentDescriptor)
        fun onPaymentError(error: MercadoPagoError)
    }

    interface OnReadyForPaymentCallback {
        fun call(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData)
    }

    interface OnEnqueueResolvedCallback {
        fun success()
        fun failure()
    }
}
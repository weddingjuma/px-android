package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.model.IPaymentDescriptor

internal interface Remedies {
    interface View {
        fun onPayButtonPressed()
        fun onPaymentFinished(payment: IPaymentDescriptor)
    }

    interface ViewModel {
        fun onPayButtonPressed()
        fun onCvvFilled(cvv: String)
    }
}
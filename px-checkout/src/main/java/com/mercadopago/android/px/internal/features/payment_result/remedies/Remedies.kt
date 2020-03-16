package com.mercadopago.android.px.internal.features.payment_result.remedies

internal interface Remedies {

    interface View {
        fun showCvvRemedy()
    }

    interface ViewModel {
        fun onCvvFilled(cvv: String)
    }
}
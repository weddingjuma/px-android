package com.mercadopago.android.px.model.internal.remedies

data class RemediesBody(
        val alternativePayerPaymentMethods: List<AlternativePayerPaymentMethod>,
        val payerPaymentMethodRejected: PayerPaymentMethodRejected
)
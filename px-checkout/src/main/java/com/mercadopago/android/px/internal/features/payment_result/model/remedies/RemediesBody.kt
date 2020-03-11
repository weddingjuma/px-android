package com.mercadopago.android.px.internal.features.payment_result.model.remedies

data class RemediesBody(
        val alternativePayerPaymentMethods: List<AlternativePayerPaymentMethod>,
        val payerPaymentMethodRejected: PayerPaymentMethodRejected
)
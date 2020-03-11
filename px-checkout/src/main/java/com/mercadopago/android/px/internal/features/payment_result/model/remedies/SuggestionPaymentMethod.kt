package com.mercadopago.android.px.internal.features.payment_result.model.remedies

data class SuggestionPaymentMethod(
        val alternativePayerPaymentMethod: AlternativePayerPaymentMethod,
        val message: String,
        val title: String
)
package com.mercadopago.android.px.model.internal.remedies

data class SuggestionPaymentMethod(
        val alternativePayerPaymentMethod: AlternativePayerPaymentMethod,
        val message: String,
        val title: String
)
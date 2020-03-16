package com.mercadopago.android.px.model.internal.remedies

data class RemediesResponse(val suggestionPaymentMethod: SuggestionPaymentMethod?) {
    private constructor(): this(null)

    companion object {
        val EMPTY = RemediesResponse()
    }
}
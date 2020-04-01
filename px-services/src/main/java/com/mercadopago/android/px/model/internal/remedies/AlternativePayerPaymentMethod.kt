package com.mercadopago.android.px.model.internal.remedies

data class AlternativePayerPaymentMethod(
        val installments: List<Installment>,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val selectedPayerCostIndex: Int
)
package com.mercadopago.android.px.internal.features.payment_result.model.remedies

data class AlternativePayerPaymentMethod(
        val installments: List<Installment>,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val selectedPayerCostIndex: Int
)
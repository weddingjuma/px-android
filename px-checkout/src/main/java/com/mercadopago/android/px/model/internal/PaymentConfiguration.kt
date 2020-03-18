package com.mercadopago.android.px.model.internal

import com.mercadopago.android.px.model.PayerCost

data class PaymentConfiguration(
    val paymentMethodId: String,
    val customOptionId: String,
    val isCard: Boolean,
    val splitPayment: Boolean,
    val payerCost: PayerCost?
)
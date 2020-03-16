package com.mercadopago.android.px.model.internal.remedies

import java.math.BigDecimal

data class Installment(
    val installments: Int,
    val labels: List<String>,
    val recommendedMessage: String,
    val totalAmount: BigDecimal
)
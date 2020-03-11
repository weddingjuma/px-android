package com.mercadopago.android.px.internal.features.payment_result.model.remedies

import java.math.BigDecimal

data class PayerPaymentMethodRejected(
    val installments: Int,
    val issuerName: String,
    val lastFourDigit: String,
    val paymentMethodId: String,
    val securityCodeLength: Int,
    val securityCodeLocation: String,
    val totalAmount: BigDecimal
)
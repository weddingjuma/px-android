package com.mercadopago.android.px.internal.util

import com.mercadopago.android.px.model.PayerCompliance

class PayerComplianceWrapper(payerCompliance: PayerCompliance?) {

    private val isIFPECompliant = isIFPECompliant(payerCompliance)

    private fun isIFPECompliant(payerCompliance: PayerCompliance?) = payerCompliance?.ifpe?.isCompliant ?: false

    fun turnedIFPECompliant(payerCompliance: PayerCompliance?) =
        !isIFPECompliant && isIFPECompliant(payerCompliance)
}
package com.mercadopago.android.px.internal.repository

interface PayerComplianceRepository {
    fun turnIFPECompliant()

    fun turnedIFPECompliant(): Boolean

    fun reset()
}
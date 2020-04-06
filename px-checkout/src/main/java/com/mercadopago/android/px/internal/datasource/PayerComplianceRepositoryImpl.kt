package com.mercadopago.android.px.internal.datasource

import android.content.SharedPreferences
import com.mercadopago.android.px.internal.repository.PayerComplianceRepository

class PayerComplianceRepositoryImpl(private val sharedPreferences: SharedPreferences) : PayerComplianceRepository {
    override fun turnIFPECompliant() = sharedPreferences.edit().putBoolean(TURNED_IFPE_COMPLIANT, true).apply()

    override fun turnedIFPECompliant() = sharedPreferences.getBoolean(TURNED_IFPE_COMPLIANT, false)

    override fun reset() = sharedPreferences.edit().remove(TURNED_IFPE_COMPLIANT).apply()

    companion object {
        const val TURNED_IFPE_COMPLIANT = "turned_ifpe_compliant"
    }
}
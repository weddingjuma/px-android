package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.repository.UserSelectionRepository
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.PaymentData
import com.mercadopago.android.px.model.internal.remedies.PayerPaymentMethodRejected
import com.mercadopago.android.px.model.internal.remedies.RemediesBody

internal class RemediesBodyMapper(private val userSelectionRepository: UserSelectionRepository)
    : Mapper<PaymentData, RemediesBody>() {

    override fun map(data: PaymentData): RemediesBody {
        val securityCodeLocation = userSelectionRepository.card?.securityCodeLocation
        with(data) {
            val payerPaymentMethodRejected = PayerPaymentMethodRejected(payerCost?.installments,
                issuer?.name, token?.lastFourDigits, paymentMethod.id, paymentMethod.paymentTypeId,
                token?.securityCodeLength, securityCodeLocation, rawAmount)
            return RemediesBody(payerPaymentMethodRejected)
        }
    }
}
package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.PayerCost
import com.mercadopago.android.px.model.internal.remedies.PayerPaymentMethodRejected
import com.mercadopago.android.px.model.internal.remedies.RemediesBody

internal class RemediesBodyMapper : Mapper<Pair<Card, PayerCost>, RemediesBody>() {

    override fun map(cardAndPayerCost: Pair<Card, PayerCost>): RemediesBody {
        val (card, payerCost) = cardAndPayerCost
        val payerPaymentMethodRejected = PayerPaymentMethodRejected(payerCost.installments,
            card.issuer?.name ?: "", card.lastFourDigits.orEmpty(), card.paymentMethod.id, card.securityCodeLength,
            card.securityCodeLocation, payerCost.totalAmount)
        return RemediesBody(payerPaymentMethodRejected)
    }
}
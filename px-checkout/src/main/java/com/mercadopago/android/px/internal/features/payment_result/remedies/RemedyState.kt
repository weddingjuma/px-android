package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy
import com.mercadopago.android.px.internal.viewmodel.PaymentModel

internal sealed class RemedyState {
    internal data class ShowCvvRemedy(val model: CvvRemedy.Model): RemedyState()
    internal object ShowKyCRemedy: RemedyState()
    internal object StartPayment: RemedyState()
    internal data class ShowResult(val paymentModel: PaymentModel): RemedyState()
}
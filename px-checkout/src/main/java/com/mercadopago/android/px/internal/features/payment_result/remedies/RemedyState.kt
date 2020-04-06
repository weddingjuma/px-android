package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.HighRiskRemedy
import com.mercadopago.android.px.internal.viewmodel.PaymentModel

internal sealed class RemedyState {
    internal data class ShowCvvRemedy(val model: CvvRemedy.Model): RemedyState()
    internal data class ShowKyCRemedy(val model: HighRiskRemedy.Model): RemedyState()
    internal data class ShowResult(val paymentModel: PaymentModel): RemedyState()
    internal data class GoToKyc(val deepLink: String): RemedyState()
    internal object ChangePaymentMethod: RemedyState()
}
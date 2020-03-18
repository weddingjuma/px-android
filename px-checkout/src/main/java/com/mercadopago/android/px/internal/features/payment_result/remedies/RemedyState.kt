package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy

internal sealed class RemedyState {
    internal data class ShowCvvRemedy(val model: CvvRemedy.Model): RemedyState()
    internal object ShowKyCRemedy: RemedyState()
}
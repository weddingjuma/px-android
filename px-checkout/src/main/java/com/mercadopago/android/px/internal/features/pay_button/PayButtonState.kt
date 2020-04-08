package com.mercadopago.android.px.internal.features.pay_button

import com.mercadopago.android.px.addons.model.SecurityValidationData
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as ButtonConfig
import com.mercadopago.android.px.model.exceptions.MercadoPagoError

internal sealed class PayButtonState

internal open class UIProgress : PayButtonState() {
    data class FingerprintRequired(val validationData: SecurityValidationData) : UIProgress()
    data class ButtonLoadingStarted(val timeOut: Int, val buttonConfig: ButtonConfig) : UIProgress()
    data class ButtonLoadingFinished(val explodeDecorator: ExplodeDecorator) : UIProgress()
    object ButtonLoadingCanceled : UIProgress()
}

internal open class UIResult : PayButtonState() {
    object VisualProcessorResult : UIResult()
    object PaymentResult : UIResult()
}

internal open class UIError : PayButtonState() {
    data class ConnectionError(val error: MercadoPagoError) : UIError()
}
package com.mercadopago.android.px.core.internal

import com.mercadopago.android.px.addons.model.SecurityValidationData
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.Reason

internal class PaymentHandler(val paymentRepository: PaymentRepository, val paymentConfiguration: PaymentConfiguration,
                              val data: SecurityValidationData, val listener: Listener) : PaymentServiceHandler {

    override fun onPaymentError(error: MercadoPagoError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCvvRequired(card: Card, reason: Reason) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVisualPayment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPaymentFinished(payment: IPaymentDescriptor) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecoverPaymentEscInvalid(recovery: PaymentRecovery?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onBiometricsResultOk() {



    }


    interface Listener {
        fun startSecurityValidation(data: SecurityValidationData)
    }
}
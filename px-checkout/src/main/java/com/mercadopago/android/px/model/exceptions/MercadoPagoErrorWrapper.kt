package com.mercadopago.android.px.model.exceptions

import java.lang.Exception

internal data class MercadoPagoErrorWrapper(private val error: MercadoPagoError) : Exception(error.message)
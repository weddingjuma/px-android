package com.mercadopago.android.px.internal.extensions

import android.text.TextUtils

fun CharSequence?.isNotNullNorEmpty() = !isNullOrEmpty()

fun CharSequence?.orIfEmpty(fallback: String) = if (isNotNullNorEmpty()) this!! else fallback
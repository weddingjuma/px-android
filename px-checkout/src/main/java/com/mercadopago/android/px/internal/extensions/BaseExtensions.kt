package com.mercadopago.android.px.internal.extensions

import android.view.View

fun CharSequence?.isNotNullNorEmpty() = !isNullOrEmpty()

fun CharSequence?.orIfEmpty(fallback: String) = if (isNotNullNorEmpty()) this!! else fallback

fun View.gone() = apply { visibility = View.GONE }

fun View.visible() = apply { visibility = View.VISIBLE }

fun View.invisible() = apply { visibility = View.INVISIBLE }

fun Any?.runIfNull(action: ()->Unit) {
    if(this == null) {
        action.invoke()
    }
}
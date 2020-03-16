package com.mercadopago.android.px.internal.features.payment_result.remedies.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.mercadopago.android.px.R

internal class CvvRemedy(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        LinearLayout(context, attrs, defStyleAttr) {

    init {
        configureView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private fun configureView(context: Context) {
        View.inflate(context, R.layout.px_remedies_cvv, null)
    }
}
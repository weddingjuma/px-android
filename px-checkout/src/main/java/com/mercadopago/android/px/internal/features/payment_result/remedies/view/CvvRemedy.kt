package com.mercadopago.android.px.internal.features.payment_result.remedies.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mercadopago.android.px.R
import kotlinx.android.synthetic.main.px_remedies_cvv.view.*

internal class CvvRemedy(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        LinearLayout(context, attrs, defStyleAttr) {

    init {
        configureView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private fun configureView(context: Context) {
        inflate(context, R.layout.px_remedies_cvv, this)
    }

    fun init(model: Model) {
        titleCvv.text = model.title
        inputCvv.filters = arrayOf(InputFilter.LengthFilter(model.length))
        inputLayout.hint = model.hint
        infoCvv.text = model.info
    }

    fun getText() = inputCvv.text

    internal data class Model(val title: String, val hint: String, val info: String, val length: Int) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(hint)
            parcel.writeString(info)
            parcel.writeInt(length)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<Model> {
            override fun createFromParcel(parcel: Parcel) = Model(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Model>(size)
        }
    }
}
package com.mercadopago.android.px.internal.features.payment_result.remedies.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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

    var listener: Listener? = null
    private var previousCvv = ""
    private var length: Int = 3

    private fun configureView(context: Context) {
        orientation = VERTICAL
        inflate(context, R.layout.px_remedies_cvv, this)
    }

    fun init(model: Model) {
        titleCvv.text = model.title
        inputCvv.filters = arrayOf(InputFilter.LengthFilter(model.length))
        inputLayout.hint = model.hint
        infoCvv.text = model.info
        length = model.length
        inputCvv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                previousCvv = text.toString()
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == length) {
                    listener?.onCvvFilled(text.toString())
                } else if (previousCvv.length == length) {
                    listener?.onCvvDeleted()
                }
            }
        })
    }

    interface Listener {
        fun onCvvFilled(cvv: String)
        fun onCvvDeleted()
    }

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
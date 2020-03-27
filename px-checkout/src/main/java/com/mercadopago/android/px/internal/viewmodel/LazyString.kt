package com.mercadopago.android.px.internal.viewmodel

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.extensions.orIfEmpty

internal data class LazyString(val text: CharSequence?, val resId: Int?) : Parcelable {
    constructor(text: CharSequence?) : this(text, null)
    constructor(resId: Int?) : this(null, resId)
    constructor(parcel: Parcel) : this(parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int)

    fun get(context: Context) = text.orIfEmpty(resId?.let { context.getString(it) } ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text?.toString())
        parcel.writeValue(resId)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<LazyString> {
        override fun createFromParcel(parcel: Parcel) = LazyString(parcel)
        override fun newArray(size: Int) = arrayOfNulls<LazyString>(size)
    }
}
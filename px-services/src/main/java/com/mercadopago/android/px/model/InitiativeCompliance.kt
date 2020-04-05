package com.mercadopago.android.px.model

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator

open class InitiativeCompliance(val isCompliant: Boolean) : KParcelable {

    protected constructor(parcel: Parcel) : this(parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeByte(if (isCompliant) 1 else 0)
    }

    companion object {
        @JvmField
        var CREATOR = parcelableCreator(::InitiativeCompliance)
    }
}
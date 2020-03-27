package com.mercadopago.android.px.model.one_tap

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.model.internal.Text

data class SliderDisplayInfo(val bottomDescription: Text) : KParcelable {

    private constructor(parcel: Parcel) : this(parcel.readParcelable<Text>(Text::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeParcelable(bottomDescription, flags)
    }

    companion object {
        @JvmField
        var CREATOR = parcelableCreator(::SliderDisplayInfo)
    }
}
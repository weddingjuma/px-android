package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class CvvRemedyResponse(val title: String, val message: String, val fieldSetting: FieldSetting) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(FieldSetting::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeParcelable(fieldSetting, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CvvRemedyResponse> {
        override fun createFromParcel(parcel: Parcel) = CvvRemedyResponse(parcel)
        override fun newArray(size: Int) = arrayOfNulls<CvvRemedyResponse?>(size)
    }

    data class FieldSetting(val name: String, val length: Int, val title: String, val hintMessage: String) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeInt(length)
            parcel.writeString(title)
            parcel.writeString(hintMessage)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<FieldSetting> {
            override fun createFromParcel(parcel: Parcel) = FieldSetting(parcel)
            override fun newArray(size: Int) = arrayOfNulls<FieldSetting?>(size)
        }
    }
}
package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class HighRiskRemedyResponse(val title: String, val message: String, val deepLink: String,
    val actionLoud: RemediesResponse.Action) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(RemediesResponse.Action::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(deepLink)
        parcel.writeParcelable(actionLoud, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<HighRiskRemedyResponse> {
        override fun createFromParcel(parcel: Parcel) = HighRiskRemedyResponse(parcel)
        override fun newArray(size: Int) = arrayOfNulls<HighRiskRemedyResponse?>(size)
    }
}
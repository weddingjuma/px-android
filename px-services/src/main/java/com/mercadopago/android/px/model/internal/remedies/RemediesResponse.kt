package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class RemediesResponse(val cvv: CvvRemedyResponse?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(CvvRemedyResponse::class.java.classLoader) as CvvRemedyResponse?)

    private constructor(): this(null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cvv, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RemediesResponse> {
        @JvmStatic val EMPTY = RemediesResponse()

        override fun createFromParcel(parcel: Parcel) = RemediesResponse(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RemediesResponse?>(size)
    }
}
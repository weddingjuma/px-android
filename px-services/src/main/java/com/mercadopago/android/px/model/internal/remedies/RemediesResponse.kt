package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class RemediesResponse(val cvv: CvvRemedy?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(CvvRemedy::class.java.classLoader) as CvvRemedy?)

    private constructor(): this(null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cvv, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemediesResponse> {
        @JvmStatic val EMPTY = RemediesResponse()

        override fun createFromParcel(parcel: Parcel): RemediesResponse {
            return RemediesResponse(parcel)
        }

        override fun newArray(size: Int): Array<RemediesResponse?> {
            return arrayOfNulls(size)
        }
    }
}
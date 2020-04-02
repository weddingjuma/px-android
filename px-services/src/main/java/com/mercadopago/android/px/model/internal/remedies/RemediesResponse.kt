package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class RemediesResponse(val cvv: CvvRemedyResponse?, val highRisk: HighRiskRemedyResponse?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CvvRemedyResponse::class.java.classLoader) as CvvRemedyResponse?,
        parcel.readParcelable(HighRiskRemedyResponse::class.java.classLoader) as HighRiskRemedyResponse?)

    private constructor(): this(null, null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cvv, flags)
        parcel.writeParcelable(highRisk, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RemediesResponse> {
        @JvmStatic val EMPTY = RemediesResponse()

        override fun createFromParcel(parcel: Parcel) = RemediesResponse(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RemediesResponse?>(size)
    }

    data class Action(val label: String): Parcelable {
        constructor(parcel: Parcel) : this(parcel.readString()!!)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(label)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<Action> {
            override fun createFromParcel(parcel: Parcel) = Action(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Action>(size)
        }
    }
}
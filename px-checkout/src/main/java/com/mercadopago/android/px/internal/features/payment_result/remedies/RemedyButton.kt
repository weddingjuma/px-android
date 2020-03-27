package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.viewmodel.LazyString

internal data class RemedyButton(val label: LazyString, val action: Action) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readParcelable(LazyString::class.java.classLoader)!!,
        Action.valueOf(parcel.readString()!!))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(label, flags)
        parcel.writeString(action.name)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RemedyButton> {
        override fun createFromParcel(parcel: Parcel) = RemedyButton(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RemedyButton>(size)
    }

    enum class Action(val id: String) {
        KYC("kyc"),
        CHANGE_PM("change_pm"),
        PAY("pay"),
        NO_ACTION("no_action")
    }
}
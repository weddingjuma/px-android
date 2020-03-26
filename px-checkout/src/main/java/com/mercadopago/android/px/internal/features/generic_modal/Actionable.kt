package com.mercadopago.android.px.internal.features.generic_modal

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator
import java.lang.IllegalStateException

data class Actionable(
    val label: String,
    val deepLink: String?,
    @GenericDialog.ActionType val action: String?) : KParcelable {

    init {
        if (deepLink.isNullOrEmpty() && action.isNullOrEmpty()) {
            throw IllegalStateException("An ${javaClass.simpleName} should have a deepLink or an action to follow")
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(label)
        writeString(deepLink)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Actionable)
    }
}
package com.mercadopago.android.px.model.internal

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator

data class Modal(
    val title: Text,
    val description: Text,
    val mainButton: Action,
    val secondaryButton: Action?,
    @SerializedName("icon_url") val imageUrl: String?) : KParcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Text::class.java.classLoader)!!,
        parcel.readParcelable(Text::class.java.classLoader)!!,
        parcel.readParcelable(Action::class.java.classLoader)!!,
        parcel.readParcelable(Action::class.java.classLoader),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeParcelable(title, flags)
        writeParcelable(description, flags)
        writeParcelable(mainButton, flags)
        writeParcelable(secondaryButton, flags)
        writeString(imageUrl)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Modal)
    }
}
package com.mercadopago.android.px.internal.features.generic_modal

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.internal.viewmodel.TextLocalized
import com.mercadopago.android.px.model.internal.Action

data class GenericDialogItem(
    val imageUrl: String?,
    val title: TextLocalized,
    val description: TextLocalized,
    val mainAction: Actionable,
    val secondaryAction: Actionable?) : KParcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(TextLocalized::class.java.classLoader)!!,
        parcel.readParcelable(TextLocalized::class.java.classLoader)!!,
        parcel.readParcelable(Action::class.java.classLoader)!!,
        parcel.readParcelable(Action::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(imageUrl)
        writeParcelable(title, flags)
        writeParcelable(description, flags)
        writeParcelable(mainAction, flags)
        writeParcelable(secondaryAction, flags)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::GenericDialogItem)
    }
}
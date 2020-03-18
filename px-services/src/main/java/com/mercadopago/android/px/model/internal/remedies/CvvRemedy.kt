package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class CvvRemedy(val title: String, val message: String, val fieldSetting: FieldSetting) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(FieldSetting::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeParcelable(fieldSetting, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CvvRemedy> {
        override fun createFromParcel(parcel: Parcel): CvvRemedy {
            return CvvRemedy(parcel)
        }

        override fun newArray(size: Int): Array<CvvRemedy?> {
            return arrayOfNulls(size)
        }
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

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<FieldSetting> {
            override fun createFromParcel(parcel: Parcel): FieldSetting {
                return FieldSetting(parcel)
            }

            override fun newArray(size: Int): Array<FieldSetting?> {
                return arrayOfNulls(size)
            }
        }
    }
}
package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.HighRiskRemedy

internal data class RemediesModel(val cvvRemedyModel: CvvRemedy.Model?,
    val highRiskRemedyModel: HighRiskRemedy.Model?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(CvvRemedy.Model::class.java.classLoader),
        parcel.readParcelable(HighRiskRemedy.Model::class.java.classLoader))

    fun hasRemedies() = cvvRemedyModel != null || highRiskRemedyModel != null

    fun getTitle() = cvvRemedyModel?.title ?: highRiskRemedyModel?.title

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cvvRemedyModel, flags)
        parcel.writeParcelable(highRiskRemedyModel, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RemediesModel> {
        override fun createFromParcel(parcel: Parcel) = RemediesModel(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RemediesModel?>(size)
    }
}
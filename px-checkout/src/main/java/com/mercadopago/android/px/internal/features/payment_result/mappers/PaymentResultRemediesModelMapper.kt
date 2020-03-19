package com.mercadopago.android.px.internal.features.payment_result.mappers

import com.mercadopago.android.px.internal.features.payment_result.remedies.RemediesModel
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy

import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.internal.remedies.RemediesResponse

internal object PaymentResultRemediesModelMapper : Mapper<RemediesResponse, RemediesModel>() {
    override fun map(model: RemediesResponse): RemediesModel {
        return RemediesModel(
                model.cvv?.let {
                    it.fieldSetting.run {
                        CvvRemedy.Model(it.message,
                                title,
                                hintMessage,
                                length)
                    }
                }
        )
    }
}
package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.arch.lifecycle.MutableLiveData
import com.mercadopago.android.px.internal.base.BaseViewModel

internal class RemediesViewModel(
        remediesModel: RemediesModel
) : BaseViewModel(), Remedies.ViewModel {

    val remedyState: MutableLiveData<RemedyState> = MutableLiveData()

    init {
        remediesModel.cvvRemedyModel?.apply {
            remedyState.value = RemedyState.ShowCvvRemedy(this@apply)
        }
    }

    override fun onCvvFilled(cvv: String) {

    }
}
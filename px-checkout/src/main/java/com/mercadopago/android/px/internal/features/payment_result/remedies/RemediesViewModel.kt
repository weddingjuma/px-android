package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import com.mercadopago.android.px.addons.ESCManagerBehaviour
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.features.pay_button.PayButton
import com.mercadopago.android.px.internal.repository.CardTokenRepository
import com.mercadopago.android.px.internal.repository.CongratsRepository
import com.mercadopago.android.px.internal.repository.CongratsRepository.PostPaymentCallback
import com.mercadopago.android.px.internal.repository.PaymentRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.util.CVVRecoveryWrapper
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.tracking.internal.events.RemedyEvent
import com.mercadopago.android.px.tracking.internal.model.RemedyTrackData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class RemediesViewModel(
    remediesModel: RemediesModel,
    private val paymentRepository: PaymentRepository,
    private val paymentSettingRepository: PaymentSettingRepository,
    private val cardTokenRepository: CardTokenRepository,
    private val escManagerBehaviour: ESCManagerBehaviour,
    private val congratsRepository: CongratsRepository,
    private val paymentMethodType: String,
    private val paymentMethodId: String
) : BaseViewModel(), Remedies.ViewModel {

    val remedyState: MutableLiveData<RemedyState> = MutableLiveData()
    private var cvv = ""

    init {
        remediesModel.cvvRemedyModel?.apply {
            RemedyEvent(RemedyTrackData(RemedyType.CVV_REQUEST.getType(), getExtraInfoTrack())).track()
            remedyState.value = RemedyState.ShowCvvRemedy(this@apply)
        }
    }

    private fun getExtraInfoTrack() = mapOf(
            "payment_method_type" to paymentMethodType,
            "payment_method_id" to paymentMethodId
    )

    override fun onPayButtonPressed(callback: PayButton.OnEnqueueResolvedCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            CVVRecoveryWrapper(cardTokenRepository, escManagerBehaviour, paymentRepository.createPaymentRecovery())
                .recoverWithCVV(cvv)?.let {
                    paymentSettingRepository.configure(it)
                    withContext(Dispatchers.Main) {
                        callback.success()
                    }
                } ?: withContext(Dispatchers.Main) {
                callback.failure()
            }
        }
    }

    fun onPaymentFinished(payment: IPaymentDescriptor) {
        congratsRepository.getPostPaymentData(payment, paymentRepository.createPaymentResult(payment),
            object : PostPaymentCallback {
                override fun handleResult(paymentModel: PaymentModel) {
                    remedyState.value = RemedyState.ShowResult(paymentModel)
                }
            })
    }

    override fun onCvvFilled(cvv: String) {
        this.cvv = cvv
    }

    override fun recoverFromBundle(bundle: Bundle) {
        super.recoverFromBundle(bundle)
        cvv = bundle.getString(EXTRA_CVV, "")
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.putString(EXTRA_CVV, cvv)
    }

    companion object {
        private const val EXTRA_CVV = "extra_cvv"
    }
}
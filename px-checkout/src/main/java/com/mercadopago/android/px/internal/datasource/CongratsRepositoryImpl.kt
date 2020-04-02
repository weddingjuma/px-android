package com.mercadopago.android.px.internal.datasource

import com.mercadopago.android.px.internal.features.payment_result.remedies.RemediesBodyMapper
import com.mercadopago.android.px.internal.repository.CongratsRepository
import com.mercadopago.android.px.internal.repository.CongratsRepository.PostPaymentCallback
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.internal.repository.UserSelectionRepository
import com.mercadopago.android.px.internal.services.CongratsService
import com.mercadopago.android.px.internal.util.StatusHelper
import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.model.*
import com.mercadopago.android.px.model.internal.PaymentReward
import com.mercadopago.android.px.model.internal.remedies.RemediesResponse
import com.mercadopago.android.px.services.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CongratsRepositoryImpl(private val congratsService: CongratsService,
        private val paymentSetting: PaymentSettingRepository, private val platform: String, private val locale: String,
        private val flow: String?, private val userSelectionRepository: UserSelectionRepository) : CongratsRepository {

    private val paymentRewardCache = HashMap<String, PaymentReward>()
    private val remediesCache = HashMap<String, RemediesResponse>()
    private val privateKey = paymentSetting.privateKey

    override fun getPostPaymentData(payment: IPaymentDescriptor, paymentResult: PaymentResult,
                                    callback: PostPaymentCallback) {
        val hasAccessToken = TextUtil.isNotEmpty(privateKey)
        val hasToReturnEmptyResponse = !hasAccessToken
        val isSuccess = StatusHelper.isSuccess(payment)
        CoroutineScope(Dispatchers.IO).launch {
            val paymentId = payment.paymentIds?.get(0) ?: payment.id.toString()
            val paymentReward = when {
                hasToReturnEmptyResponse || !isSuccess -> PaymentReward.EMPTY
                paymentRewardCache.containsKey(paymentId) -> paymentRewardCache[paymentId]!!
                else -> getPaymentReward(payment, paymentResult).apply { paymentRewardCache[paymentId] = this }
            }
            val remediesResponse = when {
                hasToReturnEmptyResponse || isSuccess -> RemediesResponse.EMPTY
                remediesCache.containsKey(paymentId) -> remediesCache[paymentId]!!
                else -> getRemedies(payment, paymentResult.paymentData).apply { remediesCache[paymentId] = this }
            }
            withContext(Dispatchers.Main) {
                handleResult(payment, paymentResult, paymentReward, remediesResponse, paymentSetting.currency, callback)
            }
        }
    }

    private suspend fun getPaymentReward(payment: IPaymentDescriptor, paymentResult: PaymentResult) =
        try {
            val joinedPaymentIds = TextUtil.join(payment.paymentIds)
            val campaignId = paymentResult.paymentData.campaign?.run { id } ?: ""
            val response = congratsService.getPaymentReward(BuildConfig.API_ENVIRONMENT, locale, privateKey,
                    joinedPaymentIds, platform, campaignId, flow).await()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                PaymentReward.EMPTY
            }
        } catch (e: Exception) {
            PaymentReward.EMPTY
        }

    private suspend fun getRemedies(payment: IPaymentDescriptor, paymentData: PaymentData) =
        try {
            val body = RemediesBodyMapper(userSelectionRepository).map(paymentData)

            val response = congratsService.getRemedies(BuildConfig.API_ENVIRONMENT_NEW, payment.id.toString(),
                locale, privateKey, body).await()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                RemediesResponse.EMPTY
            }
        } catch (e: Exception) {
            RemediesResponse.EMPTY
        }

    private fun handleResult(payment: IPaymentDescriptor, paymentResult: PaymentResult, paymentReward: PaymentReward,
                             remedies: RemediesResponse, currency: Currency, callback: PostPaymentCallback) {
        payment.process(object : IPaymentDescriptorHandler {
            override fun visit(payment: IPaymentDescriptor) {
                callback.handleResult(PaymentModel(payment, paymentResult, paymentReward, remedies, currency))
            }

            override fun visit(businessPayment: BusinessPayment) {
                callback.handleResult(BusinessPaymentModel(businessPayment, paymentResult, paymentReward, remedies,
                    currency))
            }
        })
    }
}
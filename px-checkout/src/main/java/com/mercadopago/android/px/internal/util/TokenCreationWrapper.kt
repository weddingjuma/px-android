package com.mercadopago.android.px.internal.util

import com.mercadopago.android.px.addons.ESCManagerBehaviour
import com.mercadopago.android.px.addons.model.EscDeleteReason
import com.mercadopago.android.px.internal.callbacks.TaggedCallback
import com.mercadopago.android.px.internal.extensions.isNotNullNorEmpty
import com.mercadopago.android.px.internal.repository.CardTokenRepository
import com.mercadopago.android.px.model.*
import com.mercadopago.android.px.model.exceptions.CardTokenException
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.exceptions.MercadoPagoErrorWrapper
import com.mercadopago.android.px.tracking.internal.model.Reason
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class TokenCreationWrapper private constructor(builder: Builder) {

    private val cardTokenRepository: CardTokenRepository
    private val escManagerBehaviour: ESCManagerBehaviour
    private val card: Card?
    private val token: Token?
    private val paymentMethod: PaymentMethod
    private val reason: Reason

    init {
        cardTokenRepository = builder.cardTokenRepository
        escManagerBehaviour = builder.escManagerBehaviour
        card = builder.card
        token = builder.token
        paymentMethod = builder.paymentMethod!!
        reason = builder.reason!!
    }

    suspend fun createTokenWithEsc(cvv: String): Token {
        return if (card != null) {
            SavedESCCardToken.createWithSecurityCode(card.id, cvv).run {
                validateSecurityCode(card)
                createESCToken(this).apply { lastFourDigits = card.lastFourDigits }
            }
        } else {
            SavedESCCardToken.createWithSecurityCode(token!!.cardId, cvv).run {
                validateCVVFromToken(cvv)
                createESCToken(this)
            }
        }
    }

    suspend fun createTokenWithoutEsc(cvv: String) =
        SavedCardToken(card!!.id, cvv).run {
            validateSecurityCode(card)
            createToken(this)
    }

    suspend fun cloneToken(cvv: String) = putCVV(cvv, doCloneToken().id)

    @Throws(CardTokenException::class)
    fun validateCVVFromToken(cvv: String): Boolean {
        if (token?.firstSixDigits.isNotNullNorEmpty()) {
            CardToken.validateSecurityCode(cvv, paymentMethod, token!!.firstSixDigits)
        } else if (!CardToken.validateSecurityCode(cvv)) {
            throw CardTokenException(CardTokenException.INVALID_FIELD)
        }
        return true
    }

    private suspend fun createESCToken(savedESCCardToken: SavedESCCardToken): Token {
        return suspendCoroutine {cont ->
            cardTokenRepository
                .createToken(savedESCCardToken).enqueue(object : TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                    override fun onSuccess(token: Token) {
                        if (Reason.ESC_CAP == reason) { // Remove previous esc for tracking purpose
                            escManagerBehaviour.deleteESCWith(savedESCCardToken.cardId, EscDeleteReason.ESC_CAP, null)
                        }
                        cardTokenRepository.clearCap(savedESCCardToken.cardId) { cont.resume(token) }
                    }

                    override fun onFailure(error: MercadoPagoError) {
                        cont.resumeWithException(MercadoPagoErrorWrapper(error))
                    }
                })
        }
    }

    private suspend fun createToken(savedCardToken: SavedCardToken): Token {
        return suspendCoroutine {cont ->
            cardTokenRepository
                .createToken(savedCardToken).enqueue(object : TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                    override fun onSuccess(token: Token) {
                        cont.resume(token)
                    }

                    override fun onFailure(error: MercadoPagoError) {
                        cont.resumeWithException(MercadoPagoErrorWrapper(error))
                    }
                })
        }
    }

    private suspend fun doCloneToken(): Token {
        return suspendCoroutine {cont ->
            cardTokenRepository.cloneToken(token!!.id)
                .enqueue(object : TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                    override fun onSuccess(token: Token) {
                        cont.resume(token)
                    }

                    override fun onFailure(error: MercadoPagoError) {
                        cont.resumeWithException(MercadoPagoErrorWrapper(error))
                    }
                })
        }
    }

    private suspend fun putCVV(cvv: String, tokenId: String): Token {
        return suspendCoroutine {cont ->
            cardTokenRepository.putSecurityCode(cvv, tokenId).enqueue(
                object : TaggedCallback<Token>(ApiUtil.RequestOrigin.CREATE_TOKEN) {
                    override fun onSuccess(token: Token) {
                        cont.resume(token)
                    }

                    override fun onFailure(error: MercadoPagoError) {
                        cont.resumeWithException(MercadoPagoErrorWrapper(error))
                    }
                })
        }
    }

    class Builder(val cardTokenRepository: CardTokenRepository, val escManagerBehaviour: ESCManagerBehaviour) {
        var card: Card? = null
            private set

        var token: Token? = null
            private set

        var paymentMethod: PaymentMethod? = null
            private set

        var reason: Reason? = Reason.NO_REASON
            private set

        fun with(card: Card) = apply { this.card = card }
        fun with(token: Token) = apply { this.token = token }
        fun with(paymentMethod: PaymentMethod) = apply { this.paymentMethod = paymentMethod }
        fun with(paymentRecovery: PaymentRecovery) = apply {
            card = paymentRecovery.card
            token = paymentRecovery.token
            paymentMethod = card!!.paymentMethod
            reason = Reason.from(paymentRecovery)
        }

        fun build(): TokenCreationWrapper {
            check(!(token == null && card == null)) { "Token and card can't both be null" }

            checkNotNull(paymentMethod) { "Payment method not set" }

            return TokenCreationWrapper(this)
        }
    }
}
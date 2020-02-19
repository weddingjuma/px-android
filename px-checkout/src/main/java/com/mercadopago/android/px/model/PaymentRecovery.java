package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import java.io.Serializable;

//TODO move to internal package.
public class PaymentRecovery implements Serializable {

    private final String statusDetail;
    private final Token token;
    private final Card card;

    @Deprecated
    public PaymentRecovery(final String paymentStatusDetail) {
        this(paymentStatusDetail, null, null);
    }

    @Deprecated
    public PaymentRecovery(@NonNull final String statusDetail, @NonNull final Token token) {
        this(statusDetail, token, null);
    }

    public PaymentRecovery(@NonNull final String statusDetail, @NonNull final Token token, @NonNull final Card card) {
        this.statusDetail = statusDetail;
        this.token = token;
        this.card = card;
    }

    public Token getToken() {
        return token;
    }

    public Card getCard() {
        return card;
    }

    public boolean isTokenRecoverable() {
        return Payment.StatusDetail.isStatusDetailRecoverable(statusDetail);
    }

    public boolean isStatusDetailCallForAuthorize() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(statusDetail);
    }

    public boolean isStatusDetailCardDisabled() {
        return Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED.equals(statusDetail);
    }

    public boolean isStatusDetailInvalidESC() {
        return Payment.StatusDetail.STATUS_DETAIL_INVALID_ESC.equals(statusDetail);
    }
}
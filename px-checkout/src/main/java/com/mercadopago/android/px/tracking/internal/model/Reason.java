package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.model.Cause;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.exceptions.ApiException;
import java.util.List;

public enum Reason {
    ESC_CAP,
    SAVED_CARD,
    CALL_FOR_AUTH,
    DISABLED_CARD,
    INVALID_ESC,
    INVALID_FINGERPRINT,
    UNKNOWN_TOKENIZATION_ERROR,
    NO_REASON,
    LEGACY;

    @NonNull
    public static Reason from(@NonNull final PaymentRecovery paymentRecovery) {
        if (paymentRecovery.isStatusDetailCallForAuthorize()) {
            return Reason.CALL_FOR_AUTH;
        } else if (paymentRecovery.isStatusDetailCardDisabled()) {
            return Reason.DISABLED_CARD;
        } else if (paymentRecovery.isStatusDetailInvalidESC()) {
            return Reason.ESC_CAP;
        } else {
            // this should not happen
            return Reason.NO_REASON;
        }
    }

    public static Reason from(final ApiException apiException) {
        if (apiException.getStatus() == ApiUtil.StatusCodes.BAD_REQUEST) {
            final List<Cause> causes = apiException.getCause();
            if (causes != null && !causes.isEmpty()) {
                for (final Cause cause : causes) {
                    switch (cause.getCode()) {
                    case ApiException.ErrorCodes.INVALID_ESC:
                        return INVALID_ESC;
                    case ApiException.ErrorCodes.INVALID_FINGERPRINT:
                        return INVALID_FINGERPRINT;
                    default:
                        break;
                    }
                }
            }
        }
        return UNKNOWN_TOKENIZATION_ERROR;
    }
}
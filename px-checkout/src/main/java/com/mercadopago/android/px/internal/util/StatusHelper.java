package com.mercadopago.android.px.internal.util;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.Payment;

public final class StatusHelper {

    private StatusHelper() {
    }

    public static boolean isRejected(@NonNull final IPaymentDescriptor payment) {
        if (payment instanceof BusinessPayment) {
            return ((BusinessPayment) payment).getDecorator() == BusinessPayment.Decorator.REJECTED;
        } else {
            return Payment.StatusCodes.STATUS_REJECTED.equals(payment.getPaymentStatus());
        }
    }

    public static boolean isSuccess(@NonNull final IPaymentDescriptor payment) {
        return payment instanceof BusinessPayment ? isBusinessSuccess((BusinessPayment) payment) :
            isGenericSuccess(payment.getPaymentStatus(), payment.getPaymentStatusDetail());
    }

    /* default */
    static boolean isValidStatusForEsc(final String paymentDetail) {
        switch (paymentDetail) {
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_SECURITY_CODE:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_OTHER:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_HIGH_RISK:
        case Payment.StatusDetail.STATUS_DETAIL_REJECTED_HIGH_RISK:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE:
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_MAX_ATTEMPTS:
            return false;
        default:
            return true;
        }
    }

    private static boolean isGenericSuccess(@NonNull final String statusCode, @NonNull final String statusDetail) {
        return Payment.StatusCodes.STATUS_APPROVED.equals(statusCode) ||
            (Payment.StatusCodes.STATUS_PENDING.equals(statusCode) &&
                Payment.StatusDetail.STATUS_DETAIL_PENDING_WAITING_PAYMENT.equals(statusDetail));
    }

    private static boolean isBusinessSuccess(@NonNull final BusinessPayment payment) {
        return BusinessPayment.Decorator.APPROVED == payment.getDecorator();
    }
}
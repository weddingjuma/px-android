package com.mercadopago.android.px.internal.util;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PaymentData;
import java.math.BigDecimal;

public final class PaymentDataHelper {

    private PaymentDataHelper() {
    }

    public static BigDecimal getPrettyAmountToPay(@NonNull final PaymentData paymentData) {
        if (paymentData.getDiscount() != null) {
            return paymentData.getRawAmount().subtract(paymentData.getDiscount().getCouponAmount());
        }
        return paymentData.getRawAmount();
    }
}

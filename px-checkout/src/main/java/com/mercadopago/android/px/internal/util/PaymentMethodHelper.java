package com.mercadopago.android.px.internal.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.PaymentMethod;

public final class PaymentMethodHelper {

    private PaymentMethodHelper() {
    }

    @Nullable
    public static PaymentMethod getPaymentMethodByStartWithId(@NonNull final Iterable<PaymentMethod> paymentMethods,
        @NonNull final String paymentMethodId, @NonNull final String paymentTypeId) {

        for (final PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethodId.startsWith(paymentMethod.getId())) {
                paymentMethod.setPaymentTypeId(paymentTypeId);
                return paymentMethod;
            }
        }
        return null;
    }
}

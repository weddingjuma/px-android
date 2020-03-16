package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;

public class PaymentMethodMapper extends Mapper<String, PaymentMethod> {

    @NonNull private final PaymentMethodSearch paymentMethodSearch;

    public PaymentMethodMapper(@NonNull final PaymentMethodSearch paymentMethodSearch) {
        this.paymentMethodSearch = paymentMethodSearch;
    }

    @Override
    public PaymentMethod map(@NonNull final String paymentMethodId) {
        return paymentMethodSearch.getPaymentMethodById(paymentMethodId);
    }
}
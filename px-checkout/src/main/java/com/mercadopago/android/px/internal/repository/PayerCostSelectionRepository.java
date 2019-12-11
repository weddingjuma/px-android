package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;

public interface PayerCostSelectionRepository {

    int get(@NonNull final String paymentMethodId);

    void save(@NonNull final String paymentMethodId, final int selectedPayerCost);

    void reset();
}
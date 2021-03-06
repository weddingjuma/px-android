package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.commission.PaymentTypeChargeRule;
import java.math.BigDecimal;

public interface ChargeRepository {

    @NonNull
    BigDecimal getChargeAmount(@NonNull String paymentTypeId);

    @Nullable
    PaymentTypeChargeRule getChargeRule(@NonNull String paymentTypeId);
}
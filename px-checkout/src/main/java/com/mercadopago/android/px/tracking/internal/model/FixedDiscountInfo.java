package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import java.math.BigDecimal;

public class FixedDiscountInfo extends DiscountInfo {

    private BigDecimal fixedAmount;

    public FixedDiscountInfo(@NonNull final BigDecimal fixedAmount,
        @NonNull final BigDecimal amountToDiscount, @NonNull final BigDecimal maxAmountToDiscount,
        final int maxRedeemPerUser) {
        super(DiscountType.FIXED_AMOUNT, amountToDiscount, maxAmountToDiscount, maxRedeemPerUser);
        this.fixedAmount = fixedAmount;
    }
}

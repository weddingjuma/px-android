package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import java.math.BigDecimal;

public class PercentageDiscountInfo extends DiscountInfo {

    private BigDecimal percentage;

    public PercentageDiscountInfo(@NonNull final BigDecimal percentage,
        @NonNull final BigDecimal amountToDiscount, @NonNull final BigDecimal maxAmountToDiscount,
        final int maxRedeemPerUser) {
        super(DiscountType.PERCENTAGE, amountToDiscount, maxAmountToDiscount, maxRedeemPerUser);
        this.percentage = percentage;
    }
}

package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class DiscountInfo implements Serializable {

    public enum DiscountType {
        PERCENTAGE("percentage"), FIXED_AMOUNT("fixed_amount");

        private final String description;

        DiscountType(final String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private String type;
    private BigDecimal amountToDiscount;
    private BigDecimal maxAmountToDiscount;
    private int maxRedeemPerUser;

    public DiscountInfo(@NonNull final DiscountType discountType,
        @NonNull final BigDecimal amountToDiscount, @NonNull final BigDecimal maxAmountToDiscount,
        final int maxRedeemPerUser) {
        type = discountType.toString();
        this.amountToDiscount = amountToDiscount;
        this.maxAmountToDiscount = maxAmountToDiscount;
        this.maxRedeemPerUser = maxRedeemPerUser;
    }
}

package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.PayerCost;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AvailableInstallment implements Serializable {

    @NonNull private Integer quantity;
    @NonNull private BigDecimal installmentAmount;
    @NonNull private BigDecimal visibleTotalPrice;
    @NonNull private BigDecimal interestRate;
    @NonNull private String currencyId;

    private AvailableInstallment(@NonNull final Integer quantity, @NonNull final BigDecimal installmentAmount,
        @Nullable final BigDecimal visibleTotalPrice,
        @NonNull final BigDecimal interestRate, @NonNull final String currencyId) {
        this.quantity = quantity;
        this.installmentAmount = installmentAmount;
        this.visibleTotalPrice = visibleTotalPrice;
        this.interestRate = interestRate;
        this.currencyId = currencyId;
    }

    public static List<AvailableInstallment> createFrom(@NonNull final Iterable<PayerCost> payerCostList,
        @NonNull final String currencyId) {
        final List<AvailableInstallment> list = new ArrayList<>();

        for (final PayerCost p : payerCostList) {
            final int quantity = p.getInstallments();
            final BigDecimal installmentAmount = p.getInstallmentAmount();
            final BigDecimal visibleTotalPrice = (quantity == 1 ? null : p.getTotalAmount());
            final BigDecimal interestRate = p.getInstallmentRate();

            list.add(
                new AvailableInstallment(quantity, installmentAmount, visibleTotalPrice, interestRate, currencyId));
        }

        return list;
    }

    public static AvailableInstallment createFrom(@NonNull final PayerCost payerCost,
        @NonNull final String currencyId) {
        final BigDecimal visibleTotalPrice = (payerCost.getInstallments() == 1 ? null : payerCost.getTotalAmount());
        return new AvailableInstallment(payerCost.getInstallments(), payerCost.getInstallmentAmount(),
            visibleTotalPrice, payerCost.getInstallmentRate(), currencyId);
    }
}
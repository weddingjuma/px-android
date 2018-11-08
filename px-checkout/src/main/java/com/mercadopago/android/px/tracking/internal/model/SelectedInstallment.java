package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PayerCost;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SelectedInstallment implements Serializable {

    private int quantity;
    private BigDecimal installmentAmount;
    private BigDecimal installmentRate;

    public SelectedInstallment(final int quantity, @NonNull final BigDecimal installmentAmount,
        @NonNull final BigDecimal installmentRate) {
        this.quantity = quantity;
        this.installmentAmount = installmentAmount;
        this.installmentRate = installmentRate;
    }

    public static SelectedInstallment createFrom(@NonNull final List<PayerCost> payerCosts,
        final int selectedPayerCostIndex) {
        final PayerCost payerCost = payerCosts.get(selectedPayerCostIndex);
        return new SelectedInstallment(payerCost.getInstallments(), payerCost.getInstallmentAmount(),
            payerCost.getInstallmentRate());
    }
}

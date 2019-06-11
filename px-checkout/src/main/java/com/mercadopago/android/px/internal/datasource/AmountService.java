package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.model.Discount;
import com.mercadopago.android.px.model.PayerCost;
import java.math.BigDecimal;

public class AmountService implements AmountRepository {

    @NonNull private final PaymentSettingRepository paymentSetting;
    @NonNull private final ChargeRepository chargeRepository;
    @NonNull private final DiscountRepository discountRepository;

    public AmountService(@NonNull final PaymentSettingRepository paymentSetting,
        @NonNull final ChargeRepository chargeRepository,
        @NonNull final DiscountRepository discountRepository) {
        this.paymentSetting = paymentSetting;
        this.chargeRepository = chargeRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    @NonNull
    public BigDecimal getAmountToPay(@NonNull final String paymentTypeId, @Nullable final PayerCost payerCost) {
        if (payerCost == null) {
            return amountWithoutPayerCosts(paymentTypeId);
        } else {
            return payerCost.getTotalAmount();
        }
    }

    @Override
    @NonNull
    public BigDecimal getItemsAmount() {
        return paymentSetting.getCheckoutPreference().getTotalAmount();
    }

    @NonNull
    @Override
    public BigDecimal getItemsPlusCharges(@NonNull final String paymentTypeId) {
        return paymentSetting.getCheckoutPreference().getTotalAmount()
            .add(chargeRepository.getChargeAmount(paymentTypeId));
    }

    @NonNull
    @Override
    public BigDecimal getAppliedCharges(@NonNull final String paymentTypeId) {
        return getAppliedCharges(paymentTypeId, null);
    }

    @NonNull
    @Override
    public BigDecimal getAppliedCharges(@NonNull final String paymentTypeId, @Nullable final PayerCost payerCost) {
        if (payerCost == null) {
            return chargeRepository.getChargeAmount(paymentTypeId);
        } else {
            return payerCost.getTotalAmount()
                .subtract(amountWithoutPayerCosts(paymentTypeId))
                .add(chargeRepository.getChargeAmount(paymentTypeId));
        }
    }

    @NonNull
    private BigDecimal amountWithoutPayerCosts(@NonNull final String paymentTypeId) {
        return paymentSetting.getCheckoutPreference()
            .getTotalAmount()
            .add(chargeRepository.getChargeAmount(paymentTypeId))
            .subtract(getDiscountAmount());
    }

    @NonNull
    private BigDecimal getDiscountAmount() {
        final Discount discount = discountRepository.getCurrentConfiguration().getDiscount();
        return discount == null ? BigDecimal.ZERO : discount.getCouponAmount();
    }
}
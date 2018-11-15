package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PayerCost;

public class PaymentMethodInfo {

    @SerializedName("extra_info")
    private ExtraInfo extraInfo;

    private String paymentMethodType;

    private String paymentMethodId;

    public PaymentMethodInfo(@NonNull final ExtraInfo extraInfo, @NonNull final String paymentMethodType,
        @NonNull final String paymentMethodId) {
        this.extraInfo = extraInfo;
        this.paymentMethodType = paymentMethodType;
        this.paymentMethodId = paymentMethodId;
    }

    public static PaymentMethodInfo createFrom(@NonNull final ExpressMetadata expressMetadata,
        @NonNull final String currencyId) {
        ExtraInfo extraInfo = null;
        if (expressMetadata.isCard()) {
            final int expressInstallmentIndex = expressMetadata.getCard().getDefaultPayerCostIndex();
            final PayerCost payerCost = expressMetadata.getCard().getPayerCost(expressInstallmentIndex);
            extraInfo = CardExtraInfo.createFrom(expressMetadata.getCard(), payerCost, currencyId);
        } else {
            extraInfo = new AccountMoneyInfo(expressMetadata.getAccountMoney().balance);
        }
        return new PaymentMethodInfo(extraInfo, expressMetadata.getPaymentTypeId(),
            expressMetadata.getPaymentMethodId());
    }
}

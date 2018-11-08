package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.PayerCost;

public class CardExtraInfo extends ExtraInfo {

    private AvailableInstallment selectedInstallment;
    private String cardId;
    private long issuerId;

    public CardExtraInfo(@NonNull final AvailableInstallment selectedInstallment, @NonNull final String cardId,
        final long issuerId) {
        this.selectedInstallment = selectedInstallment;
        this.cardId = cardId;
        this.issuerId = issuerId;
    }

    public static CardExtraInfo createFrom(@NonNull final CardMetadata cardMetadata, @NonNull final PayerCost payerCost,
        @NonNull final String currencyId) {
        final AvailableInstallment selectedInstallment = AvailableInstallment.createFrom(payerCost, currencyId);
        return new CardExtraInfo(selectedInstallment, cardMetadata.id, cardMetadata.displayInfo.issuerId);
    }
}

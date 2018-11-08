package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.PaymentMethodSearch;

public class CardMapper extends Mapper<ExpressMetadata, Card> {

    @NonNull private final PaymentMethodSearch paymentMethodSearch;

    public CardMapper(@NonNull final PaymentMethodSearch paymentMethodSearch) {
        this.paymentMethodSearch = paymentMethodSearch;
    }

    @Override
    public Card map(@NonNull final ExpressMetadata data) {

        final CardMetadata cardMetadata = data.getCard();
        final PaymentMethod paymentMethod =
            paymentMethodSearch.getPaymentMethodById(data.getPaymentMethodId());
        final Card card = paymentMethodSearch.getCardById(cardMetadata.id);
        card.setSecurityCode(paymentMethod != null ? paymentMethod.getSecurityCode() : null);
        card.setPaymentMethod(paymentMethod);
        return card;
    }
}

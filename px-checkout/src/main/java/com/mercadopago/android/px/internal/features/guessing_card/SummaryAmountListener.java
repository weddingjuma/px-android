package com.mercadopago.android.px.internal.features.guessing_card;

import com.mercadopago.android.px.model.Issuer;
import java.util.List;

public interface SummaryAmountListener {

    /**
     * Handles if the checkout preference has a default installment set
     */
    void onDefaultInstallmentSet();

    /**
     * Handles the automatic issuer selection scenario such as only issuer option
     */
    void onIssuerWithoutDefaultInstallment();

    /**
     * Handles a multiple issuer list
     * User must select issuer and then installments.
     */
    void onMultipleIssuers(final List<Issuer> issuers);

}

package com.mercadopago.android.px.internal.features.guessing_card;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.util.List;

public class IssuersSolver {

    @NonNull private final UserSelectionRepository userSelectionRepository;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    /**
     * Constructor
     *
     * @param paymentSettingRepository Payment setting repository
     * @param userSelectionRepository User selection repository.
     */
    public IssuersSolver(@NonNull final UserSelectionRepository userSelectionRepository,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.userSelectionRepository = userSelectionRepository;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    /**
     * Dispatches all issuers possible scenarios.
     *
     * @param listener The entity that will handle all possible flows.
     * @param issuers The list of issuers.
     */
    public void solve(@NonNull final SummaryAmountListener listener, @NonNull final List<Issuer> issuers) {
        if (issuers.size() == 1) {
            final Issuer issuer = issuers.get(0);
            // Mark as selected the issuer
            userSelectionRepository.select(issuer);
            final CheckoutPreference checkoutPreference = paymentSettingRepository.getCheckoutPreference();
            //noinspection ConstantConditions
            if (checkoutPreference.getPaymentPreference().getDefaultInstallments() == null) {
                listener.onIssuerWithoutDefaultInstallment();
            } else {
                listener.onDefaultInstallmentSet();
            }
        } else {
            listener.onMultipleIssuers(issuers);
        }
    }
}

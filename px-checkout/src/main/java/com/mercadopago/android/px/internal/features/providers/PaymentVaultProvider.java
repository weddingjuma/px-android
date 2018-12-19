package com.mercadopago.android.px.internal.features.providers;

import com.mercadopago.android.px.internal.base.ResourcesProvider;

public interface PaymentVaultProvider extends ResourcesProvider {
    String getTitle();

    String getAllPaymentTypesExcludedErrorMessage();

    String getInvalidDefaultInstallmentsErrorMessage();

    String getInvalidMaxInstallmentsErrorMessage();

    String getStandardErrorMessage();

    String getEmptyPaymentMethodsErrorMessage();
}

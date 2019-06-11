package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.preferences.CheckoutPreference;

public interface CheckoutPreferenceRepository {

    /**
     * Retrieve CheckoutPreference by Id.
     *
     * @param checkoutPreferenceId id to retrieve CheckoutPreference.
     * @return CheckoutPreference object.
     */
    MPCall<CheckoutPreference> getCheckoutPreference(@NonNull final String checkoutPreferenceId);
}

package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.internal.repository.CheckoutPreferenceRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.services.PreferenceService;
import com.mercadopago.android.px.preferences.CheckoutPreference;

import static com.mercadopago.android.px.services.BuildConfig.API_ENVIRONMENT;

public final class CheckoutPreferenceService implements CheckoutPreferenceRepository {

    @NonNull private final PreferenceService preferenceService;
    @NonNull private final PaymentSettingRepository paymentSettingRepository;

    public CheckoutPreferenceService(
        @NonNull final PreferenceService preferenceService,
        @NonNull final PaymentSettingRepository paymentSettingRepository) {
        this.preferenceService = preferenceService;
        this.paymentSettingRepository = paymentSettingRepository;
    }

    /**
     * Retrieve CheckoutPreference by Id.
     *
     * @param checkoutPreferenceId id to retrieve CheckoutPreference.
     * @return
     */
    @Override
    public MPCall<CheckoutPreference> getCheckoutPreference(@NonNull final String checkoutPreferenceId) {
        return preferenceService
            .getPreference(API_ENVIRONMENT, checkoutPreferenceId, paymentSettingRepository.getPublicKey());
    }
}

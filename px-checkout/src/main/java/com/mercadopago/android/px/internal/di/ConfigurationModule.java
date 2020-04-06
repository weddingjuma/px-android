package com.mercadopago.android.px.internal.di;

import android.content.Context;
import com.mercadopago.android.px.internal.core.ApplicationModule;
import com.mercadopago.android.px.internal.datasource.ChargeService;
import com.mercadopago.android.px.internal.datasource.DisabledPaymentMethodService;
import com.mercadopago.android.px.internal.datasource.PayerComplianceRepositoryImpl;
import com.mercadopago.android.px.internal.datasource.PayerCostSelectionRepositoryImpl;
import com.mercadopago.android.px.internal.datasource.PaymentSettingService;
import com.mercadopago.android.px.internal.datasource.UserSelectionService;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import com.mercadopago.android.px.internal.repository.DisabledPaymentMethodRepository;
import com.mercadopago.android.px.internal.repository.PayerComplianceRepository;
import com.mercadopago.android.px.internal.repository.PayerCostSelectionRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;

public final class ConfigurationModule extends ApplicationModule implements ConfigurationComponent,
    UserSelectionComponent,
    ChargeSolverComponent {

    //Mem cache
    private UserSelectionRepository userSelectionRepository;
    private PaymentSettingRepository paymentSettingRepository;
    private ChargeRepository chargeRepository;
    private DisabledPaymentMethodRepository disabledPaymentMethodRepository;
    private PayerCostSelectionRepository payerCostSelectionRepository;
    private PayerComplianceRepository payerComplianceRepository;

    public ConfigurationModule(final Context context) {
        super(context);
    }

    @Override
    public UserSelectionRepository getUserSelectionRepository() {
        if (userSelectionRepository == null) {
            userSelectionRepository = new UserSelectionService(getSharedPreferences());
        }
        return userSelectionRepository;
    }

    @Override
    public PaymentSettingRepository getPaymentSettings() {
        if (paymentSettingRepository == null) {
            paymentSettingRepository = new PaymentSettingService(getSharedPreferences());
        }
        return paymentSettingRepository;
    }

    @Override
    public ChargeRepository getChargeSolver() {
        if (chargeRepository == null) {
            chargeRepository = new ChargeService(getPaymentSettings());
        }
        return chargeRepository;
    }

    public DisabledPaymentMethodRepository getDisabledPaymentMethodRepository() {
        if (disabledPaymentMethodRepository == null) {
            disabledPaymentMethodRepository = new DisabledPaymentMethodService(getSharedPreferences());
        }
        return disabledPaymentMethodRepository;
    }

    public PayerCostSelectionRepository getPayerCostSelectionRepository() {
        if (payerCostSelectionRepository == null) {
            payerCostSelectionRepository = new PayerCostSelectionRepositoryImpl(getSharedPreferences());
        }
        return payerCostSelectionRepository;
    }

    public PayerComplianceRepository getPayerComplianceRepository() {
        if (payerComplianceRepository == null) {
            payerComplianceRepository = new PayerComplianceRepositoryImpl(getSharedPreferences());
        }
        return payerComplianceRepository;
    }

    public void reset() {
        getUserSelectionRepository().reset();
        getPaymentSettings().reset();
        getDisabledPaymentMethodRepository().reset();
        getPayerCostSelectionRepository().reset();
        getPayerComplianceRepository().reset();
    }
}
package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.core.SessionIdProvider;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.CardFormWithFragmentWrapper;
import com.mercadopago.android.px.tracking.internal.MPTracker;

public class OtherPaymentMethodPresenter extends BasePresenter<AddNewCard.View> implements AddNewCard.Actions {

    private final PaymentSettingRepository settingRepository;
    private final SessionIdProvider sessionIdProvider;

    /* default */ OtherPaymentMethodPresenter(@NonNull final PaymentSettingRepository settingRepository,
        @NonNull final SessionIdProvider sessionIdProvider) {
        this.settingRepository = settingRepository;
        this.sessionIdProvider = sessionIdProvider;
    }

    @Override
    public void onAddNewCardSelected() {
        getView().startCardForm(
            new CardFormWithFragmentWrapper(settingRepository, sessionIdProvider, MPTracker.getInstance()));
    }
}
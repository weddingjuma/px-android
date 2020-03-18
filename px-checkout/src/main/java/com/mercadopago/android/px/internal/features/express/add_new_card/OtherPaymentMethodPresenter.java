package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import com.mercadolibre.android.cardform.internal.CardFormWithFragment;
import com.mercadopago.android.px.internal.base.BasePresenter;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.tracking.internal.MPTracker;

public class OtherPaymentMethodPresenter extends BasePresenter<AddNewCard.View> implements AddNewCard.Actions {

    private final PaymentSettingRepository settingRepository;

    /* default */ OtherPaymentMethodPresenter(@NonNull final PaymentSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public void onAddNewCardSelected() {
        //TODO check for private key
        final String flowId = TextUtil.ifNotEmptyOrElse(MPTracker.getInstance().getFlowName(), "px");
        final CardFormWithFragment cardForm = CardFormWithFragment.Builder.withAccessToken(
            settingRepository.getPrivateKey(), settingRepository.getSite().getId(), flowId)
            .setExcludedTypes(settingRepository.getCheckoutPreference().getExcludedPaymentTypes()).build();
        getView().startCardForm(cardForm);
    }
}
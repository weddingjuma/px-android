package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadolibre.android.cardform.internal.CardFormWithFragment;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;

interface /* default */ AddNewCard {

    interface View extends MvpView {
        void startCardForm(@NonNull final CardFormWithFragment cardForm);

        void showPaymentMethods(@Nullable final PaymentMethodSearchItem paymentMethodSearchItem);
    }

    interface Actions {
        void onAddNewCardSelected();
    }
}
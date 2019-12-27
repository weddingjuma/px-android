package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.base.MvpView;

public interface PaymentMethod {
    interface View extends MvpView {
        void updateHighlightText(@Nullable String text);

        void disable();

        void animateHighlightMessageIn();

        void animateHighlightMessageOut();
    }

    interface Action {
        void onFocusIn();

        void onFocusOut();
    }
}
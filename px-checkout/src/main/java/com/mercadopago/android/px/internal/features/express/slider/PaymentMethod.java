package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;

public interface PaymentMethod {
    interface View extends MvpView {
        void disable(@NonNull final DisabledPaymentMethod disabledPaymentMethod);
    }

    interface Action {
        void onViewResumed();
    }
}
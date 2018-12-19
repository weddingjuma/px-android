package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;

public class ChangePaymentMethodEventTracker extends EventTracker {

    @NonNull
    @Override
    public String getEventPath() {
        return BASE_PATH + "/review/traditional/change_payment_method";
    }
}

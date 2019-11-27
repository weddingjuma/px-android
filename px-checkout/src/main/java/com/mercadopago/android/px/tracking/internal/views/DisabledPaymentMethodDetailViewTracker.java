package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;

public class DisabledPaymentMethodDetailViewTracker extends ViewTracker {
    private static final String PATH = BASE_VIEW_PATH + "/review/one_tap/disabled_payment_method_detail";

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}

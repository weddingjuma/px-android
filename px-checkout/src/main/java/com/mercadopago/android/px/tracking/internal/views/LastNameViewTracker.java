package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;

public class LastNameViewTracker extends ViewTracker {

    public static final String PATH = BASE_VIEW_PATH + PAYMENTS_PATH + "/select_method/ticket/lastname";

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}

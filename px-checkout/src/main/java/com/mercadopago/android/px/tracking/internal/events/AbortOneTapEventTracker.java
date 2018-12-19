package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;

public class AbortOneTapEventTracker extends EventTracker {
    @NonNull
    @Override
    public String getEventPath() {
        return BASE_PATH + "/review/one_tap/abort";
    }
}

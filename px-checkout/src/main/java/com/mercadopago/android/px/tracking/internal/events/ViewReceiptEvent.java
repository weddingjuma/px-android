package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.views.ResultViewTrack;

public final class ViewReceiptEvent extends EventTracker {

    private final String eventPath;

    public ViewReceiptEvent(@NonNull final ResultViewTrack resultViewTrack) {
        eventPath = resultViewTrack.getViewPath() + "/tap_view_receipt";
    }

    @NonNull
    @Override
    public String getEventPath() {
        return eventPath;
    }
}
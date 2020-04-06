package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.tracking.internal.views.ViewTracker;
import java.util.Objects;

public final class KnowYourCustomerFlowEvent extends EventTracker {

    @Nullable private String eventPath = null;

    @NonNull
    @Override
    public String getEventPath() {
        return Objects.requireNonNull(eventPath);
    }

    public void trackFromView(@NonNull final ViewTracker viewTracker) {
        eventPath = viewTracker.getViewPath() + "/start_kyc_flow";
        track();
    }

}
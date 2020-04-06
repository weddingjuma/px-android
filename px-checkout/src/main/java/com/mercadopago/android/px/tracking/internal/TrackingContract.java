package com.mercadopago.android.px.tracking.internal;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.events.EventTracker;

public interface TrackingContract {

    void trackAbort();

    void trackBack();

    void trackEvent(@NonNull final EventTracker eventTracker);
}
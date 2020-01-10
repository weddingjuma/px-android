package com.mercadopago.android.px.addons.internal;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.addons.TrackingBehaviour;
import java.util.Map;

public class TrackingDefaultBehaviour implements TrackingBehaviour {

    @Override
    public void setApplicationContext(@NonNull final String applicationContext) {
        //Do nothing
    }

    @Override
    public void onView(@NonNull final String path, @NonNull final Map<String, ?> data) {
        //Do nothing
    }

    @Override
    public void onEvent(@NonNull final String path, @NonNull final Map<String, ?> data) {
        //Do nothing
    }
}
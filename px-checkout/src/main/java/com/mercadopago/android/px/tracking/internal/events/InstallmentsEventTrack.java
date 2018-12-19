package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.tracking.internal.model.ExpressInstallmentsData;
import java.util.Map;

public class InstallmentsEventTrack extends EventTracker {

    private static final String PATH = BASE_PATH + "/review/one_tap/installments";
    @NonNull private final ExpressMetadata expressMetadata;

    public InstallmentsEventTrack(@NonNull final ExpressMetadata expressMetadata) {
        this.expressMetadata = expressMetadata;
    }

    @NonNull
    @Override
    public Map<String, Object> getEventData() {
        return ExpressInstallmentsData.createFrom(expressMetadata).toMap();
    }

    @NonNull
    @Override
    public String getEventPath() {
        return PATH;
    }
}

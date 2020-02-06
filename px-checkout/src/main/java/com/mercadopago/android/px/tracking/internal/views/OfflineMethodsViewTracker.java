package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import java.util.Map;

public final class OfflineMethodsViewTracker extends ViewTracker {

    public static final String PATH_REVIEW_OFFLINE_METHODS_VIEW = BASE_VIEW_PATH + "review/one_tap/offline_methods";

    private final OfflineMethodsData data;

    public OfflineMethodsViewTracker(final OfflinePaymentTypesMetadata offlinePaymentTypesMetadata) {
        data = OfflineMethodsData.createFrom(offlinePaymentTypesMetadata);
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        return data.toMap();
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH_REVIEW_OFFLINE_METHODS_VIEW;
    }
}

package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.internal.model.OneTapData;
import java.util.Map;

public class OneTapViewTracker extends ViewTracker {

    public static final String PATH_REVIEW_ONE_TAP_VIEW = BASE_VIEW_PATH + "/review/one_tap";

    private final Map<String, Object> data;

    public OneTapViewTracker(final Iterable<ExpressMetadata> expressMetadataList,
        @NonNull final CheckoutPreference checkoutPreference,
        @NonNull final DiscountRepository discountRepository) {
        data = OneTapData.createFrom(expressMetadataList, checkoutPreference, discountRepository).toMap();
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH_REVIEW_ONE_TAP_VIEW;
    }
}

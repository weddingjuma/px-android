package com.mercadopago.android.px.internal.features.business_result;

import android.support.annotation.Nullable;
import com.mercadolibre.android.mlbusinesscomponents.components.discount.MLBusinessDiscountTracker;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class BusinessPaymentResultTracker implements MLBusinessDiscountTracker {

    private static final String BASE_PATH = "/discount_center/payers/touchpoint/px-congrats/";

    @Override
    public void track(@Nullable final String action, @Nullable final Map<String, Object> eventData) {
        if (shouldTrack(action, eventData)) {
            MPTracker.getInstance().trackEvent(createPath(action), eventData);
        }
    }

    private boolean shouldTrack(@Nullable final String action, @Nullable final Map<String, Object> eventData) {
        return !StringUtils.isEmpty(action)
            && eventData != null
            && !eventData.isEmpty();
    }

    private String createPath(final String action) {
        return BASE_PATH + action;
    }
}

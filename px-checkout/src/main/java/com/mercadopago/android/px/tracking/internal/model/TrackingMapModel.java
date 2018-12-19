package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.JsonUtil;
import java.util.Map;

public class TrackingMapModel {

    @NonNull
    public Map<String, Object> toMap() {
        return toMap(this);
    }

    public static Map<String, Object> toMap(final Object object) {
        return JsonUtil.getInstance().getMapFromObject(object);
    }
}

package com.mercadopago.android.px.tracking.internal.events;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.tracking.internal.MPTracker;
import com.mercadopago.android.px.tracking.internal.model.InitData;
import java.util.Map;
import java.util.UUID;

public final class InitEvent extends EventTracker {

    @NonNull private final InitData initData;
    @NonNull private final String sessionId;

    public InitEvent(@NonNull final PaymentSettingRepository paymentSettingRepository) {
        initData = InitData.from(paymentSettingRepository);
        sessionId = UUID.randomUUID().toString();
    }

    @NonNull
    @Override
    public String getEventPath() {
        return BASE_PATH + "/init";
    }

    @NonNull
    @Override
    public Map<String, Object> getEventData() {
        return initData.toMap();
    }

    @Override
    public final void track() {
        MPTracker.getInstance().setSessionId(sessionId);
        super.track();
    }
}

package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.tracking.internal.events.ConfirmEvent;

@SuppressWarnings("unused")
@Keep
public class ConfirmData extends AvailableMethod {

    private final String reviewType;

    public ConfirmData(@NonNull final ConfirmEvent.ReviewType reviewType,
        @NonNull final AvailableMethod availableMethod) {
        super(availableMethod.paymentMethodId, availableMethod.paymentMethodType, availableMethod.extraInfo);
        this.reviewType = reviewType.value;
    }
}

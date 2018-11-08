package com.mercadopago.android.px.tracking.mocks;

import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.TrackingIntent;
import com.mercadopago.android.px.tracking.internal.services.MPTrackingService;

public class MPMockedTrackingService implements MPTrackingService {

    @Override
    public void trackToken(final TrackingIntent trackingIntent) {

    }

    @Override
    public void trackPaymentId(final PaymentIntent paymentIntent) {

    }
}

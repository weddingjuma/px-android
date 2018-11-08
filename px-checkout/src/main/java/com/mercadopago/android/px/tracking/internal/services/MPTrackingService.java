package com.mercadopago.android.px.tracking.internal.services;

import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.TrackingIntent;

/**
 * Created by vaserber on 7/3/17.
 */

public interface MPTrackingService {

    void trackToken(TrackingIntent trackingIntent);

    void trackPaymentId(PaymentIntent paymentIntent);
}

package com.mercadopago.android.px.tracking.internal.services;

import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.TrackingIntent;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vaserber on 6/5/17.
 */

public interface TrackingAPI {

    @POST("/{version}/checkout/tracking")
    Call<Void> trackToken(@Path(value = "version", encoded = true) String version, @Body TrackingIntent body);

    @POST("/{version}/checkout/tracking/off")
    Call<Void> trackPaymentId(@Path(value = "version", encoded = true) String version, @Body PaymentIntent body);
}

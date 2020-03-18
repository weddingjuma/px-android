package com.mercadopago.android.px.internal.services;

import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.internal.PaymentReward;
import com.mercadopago.android.px.model.internal.remedies.RemediesBody;
import com.mercadopago.android.px.model.internal.remedies.RemediesResponse;
import kotlinx.coroutines.Deferred;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CongratsService {

    @GET("/{version}/px_mobile/congrats")
    Deferred<Response<PaymentReward>> getPaymentReward(
        @Path(value = "version", encoded = true) String version,
        @Header("Accept-Language") String locale,
        @Query("access_token") String accessToken,
        @Query("payment_ids") String paymentIds,
        @Query("platform") String platform,
        @Query("campaign_id") String campaignId,
        @Nullable @Query("flow_name") String flowName);

    @POST("{environment}/px_mobile/v1/remedies/{payment_id}")
    Deferred<Response<RemediesResponse>> getRemedies(
        @Path(value = "environment", encoded = true) String environment,
        @Path(value = "payment_id", encoded = true) String paymentId,
        @Header("Accept-Language") String locale,
        @Query("access_token") String accessToken,
        @Body RemediesBody body);
}
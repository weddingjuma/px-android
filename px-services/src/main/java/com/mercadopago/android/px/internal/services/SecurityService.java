package com.mercadopago.android.px.internal.services;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.ThreeDSChallenge;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SecurityService {
    @POST("/cardholder_authenticator/trxAuthentication/{card_token}")
    MPCall<ThreeDSChallenge> getChallengeRequest(
        @Header("X-Auth-Token") String token,
        @Body Map<String,Object> body,
        @Path(value = "card_token", encoded = true) String cardToken);
}

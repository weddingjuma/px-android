package com.mercadopago.android.px.internal.services;

import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.ThreeDSChallenge;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ThreeDSService {
    @POST("/cardholder_authenticator/trxAuthentication/f11daf8d30af46ec62e1a1bc4b3d8f5d")
    MPCall<ThreeDSChallenge> getChallengeRequest( @Header("X-Auth-Token") String token, @Body Map<String,Object> body);
}

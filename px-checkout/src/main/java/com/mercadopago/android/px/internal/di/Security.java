package com.mercadopago.android.px.internal.di;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.mercadopago.android.px.internal.services.ThreeDSService;
import com.mercadopago.android.px.internal.util.RetrofitUtil;
import com.mercadopago.android.px.model.ThreeDS.SDKEphemeralPublicKey;
import com.mercadopago.android.px.model.ThreeDSChallenge;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.tracking.internal.utils.JsonConverter;
import com.ndsthreeds.android.sdk.NdsThreeDS2ServiceImpl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.emvco.threeds.core.AuthenticationRequestParameters;
import org.emvco.threeds.core.ChallengeParameters;
import org.emvco.threeds.core.ChallengeStatusReceiver;
import org.emvco.threeds.core.CompletionEvent;
import org.emvco.threeds.core.ConfigParameters;
import org.emvco.threeds.core.ProtocolErrorEvent;
import org.emvco.threeds.core.RuntimeErrorEvent;
import org.emvco.threeds.core.ThreeDS2Service;
import org.emvco.threeds.core.Transaction;
import org.emvco.threeds.core.exceptions.InvalidInputException;
import org.emvco.threeds.core.exceptions.SDKAlreadyInitializedException;
import org.emvco.threeds.core.exceptions.SDKRuntimeException;
import org.emvco.threeds.core.ui.UiCustomization;
import retrofit2.Retrofit;

public final class Security {

    private static final String FURY_TOKEN =
        "da32929c742dff044b705c0691d7e4fed09c0d50a6cb4e41aad67ae0e85219b7";
    private static final String CARD_TOKEN_TEST = "59b9c8be158b95e21177ff2d74f44da4";
    private static final String BASE_URL =
        "http://chatest.mpcs-cardholder-authenticator.melifrontends.com";
    private Retrofit client;
    private ThreeDSService service;
    private static Security instance;
    private ThreeDS2Service threeDS2Service;

    private Security() {
    }

    public static Security getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                "Session is not initialized. Make sure to call Security.initialize(Context) first.");
        }
        return instance;º
    }

    public static void initialize() {
        instance = new Security();
        instance.threeDS2Service = new NdsThreeDS2ServiceImpl();

        final ConfigParameters configParameters = new ConfigParameters();
        final UiCustomization uiCustomization = new UiCustomization();

        final String locale = "es_AR";

        try {
            instance.threeDS2Service.initialize(
                Session.getInstance().getApplicationContext(), configParameters, locale,
                uiCustomization);
        } catch (final InvalidInputException e) {
            Log.e("3DS", e.getMessage());
        } catch (final SDKAlreadyInitializedException e) {
            Log.e("3DS", e.getMessage());
        } catch (final SDKRuntimeException e) {
            Log.e("3DS", e.getMessage());
        }

        instance.client =
            RetrofitUtil.getRetrofitClient(Session.getInstance().getApplicationContext(), BASE_URL);
        instance.service = instance.client.create(ThreeDSService.class);
    }

    public String getSDKVersion() {
        return threeDS2Service.getSDKVersion();
    }

    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    Map<String, Object> makeBody(AuthenticationRequestParameters params) {
        final Map<String, Object> deviceRenderOptions = new HashMap<>();
        deviceRenderOptions.put("sdkInterface", "01");
        final ArrayList<String> sdkUiType = new ArrayList<>();
        sdkUiType.add("01");
        sdkUiType.add("02");
        sdkUiType.add("03");
        deviceRenderOptions.put("sdkUiType", sdkUiType);

        final Map<String, Object> data = new HashMap<>();
        data.put("AuthTransaction", "01");
        data.put("purchaseAmount", "12300");
        data.put("purchaseCurrency", "840");
        data.put("purchaseDate", getDate());
        data.put("purchaseExponent", 2);
        data.put("mcc", "0001");
        data.put("mechantCountryCode", "840");
        data.put("sdkAppID", params.getSDKAppID());
        data.put("sdkEncData", params.getDeviceData());

        final SDKEphemeralPublicKey sdkEphemeralPublicKey =
            JsonConverter.getInstance()
                .fromJson(params.getSDKEphemeralPublicKey(), SDKEphemeralPublicKey.class);
        data.put("sdkEphemPubKey", sdkEphemeralPublicKey);

        data.put("sdkMaxTimeout", "06");
        data.put("sdkReferenceNumber", params.getSDKReferenceNumber());
        data.put("sdkTransID", params.getSDKTransactionID());
        data.put("deviceRenderOptions", deviceRenderOptions);

        final Map<String, Object> body = new HashMap<>();
        body.put("protocol", "3DS");
        body.put("version", params.getMessageVersion());
        body.put("DeviceChannel", "APP");
        body.put("UserID", 417995560);
        body.put("data", data);

        return body;
    }

    public void askForChallenge(final Activity activity) {
        final Transaction transaction = threeDS2Service.createTransaction("A000000004", "2.1.0");
        final AuthenticationRequestParameters params =
            transaction.getAuthenticationRequestParameters();
        final ChallengeParameters challengeParameters = new ChallengeParameters();

        final ProgressDialog progress = transaction.getProgressView(activity);
        progress.show();

        service.getChallengeRequest(FURY_TOKEN, makeBody(params), CARD_TOKEN_TEST)
            .enqueue(new Callback<ThreeDSChallenge>() {
                @Override
                public void success(final ThreeDSChallenge threeDSChallenge) {
                    challengeParameters.setAcsSignedContent(threeDSChallenge.acsSignedContent);
                    challengeParameters.setAcsTransactionID(threeDSChallenge.acsTransID);
                    challengeParameters.setAcsRefNumber(threeDSChallenge.acsReferenceNumber);
                    challengeParameters
                        .set3DSServerTransactionID(threeDSChallenge.threeDSServerTransID);

                    transaction
                        .doChallenge(activity, challengeParameters, new ChallengeStatusReceiver() {
                            @Override
                            public void completed(final CompletionEvent completionEvent) {
                                Intent intent = new Intent(activity, activity.getClass());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("completed",true);
                                activity.startActivity(intent);
                            }

                            @Override
                            public void cancelled() {
                                Intent intent = new Intent(activity, activity.getClass());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("completed",false);
                                activity.startActivity(intent);

                            }

                            @Override
                            public void timedout() {
                                Toast.makeText(activity, "timedout", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void protocolError(final ProtocolErrorEvent protocolErrorEvent) {
                                Toast.makeText(activity,
                                    protocolErrorEvent.getErrorMessage().getErrorDetails(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            }

                            @Override
                            public void runtimeError(final RuntimeErrorEvent runtimeErrorEvent) {
                                Toast.makeText(activity, runtimeErrorEvent.getErrorMessage(),
                                    Toast.LENGTH_SHORT).show();
                            }
                        }, 5);
                }

                @Override
                public void failure(final ApiException apiException) {

                }
            });
    }
}

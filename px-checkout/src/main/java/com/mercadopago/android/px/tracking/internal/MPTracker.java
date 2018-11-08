package com.mercadopago.android.px.tracking.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.AppInformation;
import com.mercadopago.android.px.model.DeviceInfo;
import com.mercadopago.android.px.model.Event;
import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.ScreenViewEvent;
import com.mercadopago.android.px.model.TrackingIntent;
import com.mercadopago.android.px.tracking.PXEventListener;
import com.mercadopago.android.px.tracking.PXTrackingListener;
import com.mercadopago.android.px.tracking.internal.services.MPTrackingService;
import com.mercadopago.android.px.tracking.internal.services.MPTrackingServiceImpl;
import com.mercadopago.android.px.tracking.internal.utils.TrackingUtil;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public final class MPTracker {

    /**
     * This singleton instance is safe because session will work with
     * application context. Application context it's never leaking.
     */
    @SuppressLint("StaticFieldLeak") private static MPTracker mMPTrackerInstance;

    private PXEventListener mPXEventListener;

    private PXTrackingListener pxTrackingListener;

    private MPTrackingService mMPTrackingService;

    /**
     * Added in 4.3.0 version - temporal replacement for tracking additional params.
     */
    @Nullable private Map<String, ?> flowDetail;
    /**
     * Added in 4.3.0 version - temporal replacement for tracking additional params.
     */
    @Nullable private String flowName;

    private String mPublicKey;
    private String mSdkVersion;
    private String mSiteId;
    private Context mContext;

    private static final String SDK_PLATFORM = "Android";
    private static final String SDK_TYPE = "native";
    private static final String DEFAULT_FLAVOUR = "3";

    private static final String FLOW_DETAIL_KEY = "flow_detail";
    private static final String FLOW_NAME_KEY = "flow";

    private Boolean trackerInitialized = false;

    private MPTracker() {
        //Do nothing
    }

    public static synchronized MPTracker getInstance() {
        if (mMPTrackerInstance == null) {
            mMPTrackerInstance = new MPTracker();
        }
        return mMPTrackerInstance;
    }

    private void initializeMPTrackingService() {
        if (mMPTrackingService == null) {
            mMPTrackingService = new MPTrackingServiceImpl();
        }
    }

    /**
     * Set listener to track library's screens and events in the app.
     *
     * @param PXEventListener PXEventListener implementing the tracking methods
     * @deprecated Deprecated due to new tracking implementation standards.
     * Use {@link com.mercadopago.android.px.tracking.internal.MPTracker#setPXTrackingListener(PXTrackingListener)} instead.
     */
    @Deprecated
    public void setTracksListener(final PXEventListener PXEventListener) {
        mPXEventListener = PXEventListener;
    }

    /**
     * Set listener to track library's screens and events in the app.
     *
     * @param pxTrackingListener implementing the tracking methods
     */
    public void setPXTrackingListener(final PXTrackingListener pxTrackingListener) {
        this.pxTrackingListener = pxTrackingListener;
    }

    /**
     * Set a map to add information to the library's screen and event tracks.
     *
     * @param flowDetail A map with extra information about the flow in your app that uses the checkout.
     */
    public void setFlowDetail(@NonNull final Map<String, ? extends Object> flowDetail) {
        this.flowDetail = flowDetail;
    }

    /**
     * Set a name to identify the flow in your app that opens the checkout.
     *
     * @param flowName The name that identifies your flow
     */
    public void setFlowName(@Nullable final String flowName) {
        this.flowName = flowName;
    }

    /**
     * @param paymentId The payment id of a payment method off. Cannot be {@code null}.
     * @param typeId The payment type id. It has to be a card type.
     */
    public PaymentIntent trackPayment(final Long paymentId, final String typeId) {

        PaymentIntent paymentIntent = null;

        if (trackerInitialized) {
            paymentIntent = new PaymentIntent(mPublicKey, paymentId.toString(), DEFAULT_FLAVOUR, SDK_PLATFORM, SDK_TYPE,
                mSdkVersion, mSiteId);
            initializeMPTrackingService();
            mMPTrackingService.trackPaymentId(paymentIntent);
        }
        return paymentIntent;
    }

    /**
     * @param token The card token id of a payment. Cannot be {@code null}.
     */
    public TrackingIntent trackToken(final String token) {
        TrackingIntent trackingIntent = null;
        if (trackerInitialized && !isEmpty(token)) {
            trackingIntent =
                new TrackingIntent(mPublicKey, token, DEFAULT_FLAVOUR, SDK_PLATFORM, SDK_TYPE, mSdkVersion, mSiteId);
            initializeMPTrackingService();
            mMPTrackingService.trackToken(trackingIntent);
        }
        return trackingIntent;
    }

    /**
     * This method tracks a list of events in one request
     *
     * @param appInformation Info about this application and SDK integration
     * @param deviceInfo Info about the device that is using the app
     * @param event Event to track
     * @param context Application context
     * @deprecated Old tracking listener.
     */
    @Deprecated
    public void trackEvent(final String publicKey,
        final AppInformation appInformation,
        final DeviceInfo deviceInfo,
        final Event event,
        final Context context) {
        trackEvent(publicKey, appInformation, deviceInfo, event, context, StrategyMode.NOOP_STRATEGY);
    }

    /**
     * This method tracks a list of events in one request
     *
     * @param appInformation Info about this application and SDK integration
     * @param deviceInfo Info about the device that is using the app
     * @param event Event to track
     * @param context Application context
     * @deprecated Old tracking listener.
     */
    @Deprecated
    public void trackEvent(final String publicKey,
        final AppInformation appInformation,
        final DeviceInfo deviceInfo,
        final Event event,
        final Context context,
        final String trackingStrategy) {

        initializeMPTrackingService();

        mContext = context;

        if (event.getType().equals(Event.TYPE_SCREEN_VIEW)) {
            final ScreenViewEvent screenViewEvent = (ScreenViewEvent) event;
            trackOldView(screenViewEvent.getScreenId());
            //New listener tracking compatible.
            trackViewCompat(screenViewEvent.getScreenId());
        }
    }

    private void trackViewCompat(@NonNull final String path) {
        if (pxTrackingListener != null) {
            pxTrackingListener.onView(path, flowDetail == null ? new HashMap<String, Object>() : flowDetail);
        }
    }

    private void trackOldView(@NonNull final String screenName) {
        if (mPXEventListener != null) {
            mPXEventListener.onScreenLaunched(screenName, new HashMap<String, String>());
        }
    }

    public void trackView(@NonNull final String path, @NonNull final Map<String, Object> data) {
        addAdditionalFlowInfo(data);
        if (pxTrackingListener != null) {
            pxTrackingListener.onView(path, data);
        }
        //Old tracking Compatibility.
        trackOldView(path);
    }

    public void trackEvent(@NonNull final String path, @NonNull final Map<String, Object> data) {

        if (pxTrackingListener != null) {
            // Event friction case needs to add flow detail in a different way. We ignore this case for now.
            if (!TrackingUtil.EVENT_PATH_FRICTION.equals(path)) {
                addAdditionalFlowInfo(data);
            }
            pxTrackingListener.onEvent(path, data);
        }
    }

    private void addAdditionalFlowInfo(@NonNull final Map<String, Object> data) {
        data.put(FLOW_DETAIL_KEY, flowDetail);
        data.put(FLOW_NAME_KEY, flowName);
    }

    /**
     * @param publicKey The public key of the merchant. Cannot be {@code null}.
     * @param siteId The site that comes in the preference. Cannot be {@code null}.
     * @param sdkVersion The Mercado Pago sdk version. Cannot be {@code null}.
     * @param context Reference to Android Context. Cannot be {@code null}.
     */
    public void initTracker(final String publicKey,
        final String siteId,
        final String sdkVersion,
        final Context context) {

        if (!isTrackerInitialized()) {
            if (areInitParametersValid(publicKey, siteId, sdkVersion, context)) {
                trackerInitialized = true;
                mPublicKey = publicKey;
                mSiteId = siteId;
                mSdkVersion = sdkVersion;
                mContext = context.getApplicationContext();
            }
        }
    }

    /**
     * @param publicKey The public key of the merchant. Cannot be {@code null}.
     * @param siteId The site that comes in the preference. Cannot be {@code null}.
     * @param sdkVersion The Mercado Pago sdk version. Cannot be {@code null}.
     * @param context Reference to Android Context. Cannot be {@code null}.
     * @return True if all parameters are valid. False if any parameter is invalid
     */
    private boolean areInitParametersValid(final String publicKey,
        final String siteId,
        final String sdkVersion,
        final Context context) {

        return !isEmpty(publicKey) && !isEmpty(sdkVersion) && !isEmpty(siteId) && context != null;
    }

    /**
     * Check if MPTracker is initialized
     *
     * @return True if is initialized. False if is not initialized.
     */
    private boolean isTrackerInitialized() {
        return mPublicKey != null && mSdkVersion != null && mSiteId != null && mContext != null;
    }
}
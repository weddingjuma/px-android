package com.mercadopago.android.px.tracking.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.BuildConfig;
import com.mercadopago.android.px.model.Event;
import com.mercadopago.android.px.model.PaymentIntent;
import com.mercadopago.android.px.model.ScreenViewEvent;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.TrackingIntent;
import com.mercadopago.android.px.tracking.PXEventListener;
import com.mercadopago.android.px.tracking.PXTrackingListener;
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker;
import com.mercadopago.android.px.tracking.internal.services.MPTrackingService;
import com.mercadopago.android.px.tracking.internal.services.MPTrackingServiceImpl;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public final class MPTracker {

    private static final String ATTR_EXTRA_INFO = "extra_info";
    private static final String ATTR_FLOW_DETAIL = "flow_detail";
    private static final String ATTR_FLOW_NAME = "flow";

    @Deprecated
    private static final String SDK_PLATFORM = "Android";
    @Deprecated
    private static final String SDK_TYPE = "native";
    @Deprecated
    private static final String DEFAULT_FLAVOUR = "3";

    private static MPTracker mMPTrackerInstance;

    @Deprecated
    @Nullable private PXEventListener mPXEventListener;

    @Nullable private PXTrackingListener pxTrackingListener;

    /**
     * Added in 4.3.0 version - temporal replacement for tracking additional params.
     */
    @Nullable private Map<String, ?> flowDetail;
    /**
     * Added in 4.3.0 version - temporal replacement for tracking additional params.
     */
    @Nullable private String flowName;

    /**
     * Service to track Off payments + card tokens
     */
    @NonNull private final MPTrackingService mMPTrackingService;

    private MPTracker() {
        mMPTrackingService = new MPTrackingServiceImpl();
    }

    public static synchronized MPTracker getInstance() {
        if (mMPTrackerInstance == null) {
            mMPTrackerInstance = new MPTracker();
        }
        return mMPTrackerInstance;
    }

    /**
     * Set listener to track library's screens and events in the app.
     *
     * @param pxEventListener implementing the tracking methods
     * @deprecated Deprecated due to new tracking implementation standards. Use {@link com.mercadopago.android.px.tracking.internal.MPTracker#setPXTrackingListener(PXTrackingListener)}
     * instead.
     */
    @Deprecated
    public void setTracksListener(@Nullable final PXEventListener pxEventListener) {
        mPXEventListener = pxEventListener;
    }

    /**
     * Set listener to track library's screens and events in the app.
     *
     * @param pxTrackingListener implementing the tracking methods
     */
    public void setPXTrackingListener(@Nullable final PXTrackingListener pxTrackingListener) {
        this.pxTrackingListener = pxTrackingListener;
    }

    /**
     * Set a map to add information to the library's screen and event tracks.
     *
     * @param flowDetail A map with extra information about the flow in your app that uses the checkout.
     */
    public void setFlowDetail(@NonNull final Map<String, ?> flowDetail) {
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
     * @param publicKey payment public key
     * @param site site
     */
    public void trackPayment(final Long paymentId, final String publicKey,
        final Site site) {
        final PaymentIntent paymentIntent =
            new PaymentIntent(publicKey, paymentId.toString(), DEFAULT_FLAVOUR, SDK_PLATFORM, SDK_TYPE,
                BuildConfig.VERSION_NAME, site.getId());
        mMPTrackingService.trackPaymentId(paymentIntent);
    }

    /**
     * @param tokenId The card token id of a payment. Cannot be {@code null}.
     */
    public void trackTokenId(@NonNull final String tokenId, @NonNull final String publicKey, @NonNull final Site site) {
        if (!isEmpty(tokenId)) {
            final TrackingIntent trackingIntent =
                new TrackingIntent(publicKey, tokenId, DEFAULT_FLAVOUR, SDK_PLATFORM, SDK_TYPE,
                    BuildConfig.VERSION_NAME,
                    site.getId());
            mMPTrackingService.trackToken(trackingIntent);
        }
    }

    /**
     * This method tracks a list of events in one request
     *
     * @param event Event to track
     * @deprecated Old tracking listener.
     */
    @Deprecated
    public void trackEvent(final Event event) {
        if (event.getType().equals(Event.TYPE_SCREEN_VIEW)) {
            final ScreenViewEvent screenViewEvent = (ScreenViewEvent) event;
            trackOldView(screenViewEvent.getScreenId());
            //New listener tracking compatible.
            trackViewCompat(screenViewEvent.getScreenId());
        }
    }

    private void trackViewCompat(@NonNull final String path) {
        if (pxTrackingListener != null) {
            final Map<String, Object> data = new HashMap<>();
            addAdditionalFlowInfo(data);
            pxTrackingListener.onView(path, data);
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
            if (!FrictionEventTracker.PATH.equals(path)) {
                addAdditionalFlowInfo(data);
            } else {
                addAdditionalFlowIntoExtraInfo(data);
            }
            pxTrackingListener.onEvent(path, data);
        }
    }

    private void addAdditionalFlowIntoExtraInfo(@NonNull final Map<String, Object> data) {
        if (data.containsKey(ATTR_EXTRA_INFO)) {
            final Object o = data.get(ATTR_EXTRA_INFO);
            try {
                final Map<String, Object> value = (Map<String, Object>) o;
                value.put(ATTR_FLOW_NAME, flowName);
            } catch (final ClassCastException e) {
                // do nothing.
            }
        }
    }

    private void addAdditionalFlowInfo(@NonNull final Map<String, Object> data) {
        data.put(ATTR_FLOW_DETAIL, flowDetail);
        data.put(ATTR_FLOW_NAME, flowName);
    }
}
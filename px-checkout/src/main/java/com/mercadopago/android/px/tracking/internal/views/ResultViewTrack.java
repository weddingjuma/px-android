package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.internal.model.ResultViewTrackModel;
import java.util.Locale;
import java.util.Map;

public class ResultViewTrack extends ViewTracker {

    private static final String PATH = BASE_VIEW_PATH + "/result/%s";

    private static final String SUCCESS = "success";
    private static final String PENDING = "further_action_needed";
    private static final String ERROR = "error";
    private static final String UNKNOWN = "unknown";

    private final ResultViewTrackModel resultViewTrackModel;
    private final PaymentResult payment;

    public enum Style {
        GENERIC("generic"),
        CUSTOM("custom");

        @NonNull public final String value;

        Style(@NonNull final String value) {
            this.value = value;
        }
    }

    public ResultViewTrack(@NonNull final Style style, @NonNull final PaymentResult payment,
        @NonNull final CheckoutPreference checkoutPreference) {
        resultViewTrackModel =
            new ResultViewTrackModel(style, payment, checkoutPreference);
        this.payment = payment;
    }

    private String getMappedResult(@NonNull final PaymentResult payment) {
        if (payment.isApproved() || payment.isInstructions()) {
            return SUCCESS;
        } else if (payment.isRejected()) {
            return ERROR;
        } else if (payment.isPending()) {
            return PENDING;
        } else {
            return UNKNOWN;
        }
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        final Map<String, Object> data = resultViewTrackModel.toMap();
        data.put("payment_id", resultViewTrackModel.getPaymentId());

        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return String.format(Locale.US, PATH, getMappedResult(payment));
    }
}

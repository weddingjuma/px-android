package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.tracking.internal.mapper.FromPaymentMethodToAvailableMethods;
import java.util.Locale;
import java.util.Map;

public class ResultViewTrack extends ViewTracker {

    private static final String PATH = BASE_VIEW_PATH + "/result/%s";
    private static final String SUCCESS = "success";
    private static final String PENDING = "further_action_needed";
    private static final String ERROR = "error";
    private static final String UNKNOWN = "unknown";

    private static final String ATTR_STYLE = "style";
    private static final String ATTR_PAYMENT_ID = "payment_id";
    private static final String ATTR_PAYMENT_STATUS = "payment_status";
    private static final String ATTR_PAYMENT_STATUS_DETAIL = "payment_status_detail";

    @NonNull private final Style style;
    @NonNull private final PaymentResult payment;

    public enum Style {
        GENERIC("generic"),
        CUSTOM("custom");

        @NonNull public final String value;

        Style(@NonNull final String value) {
            this.value = value;
        }
    }

    public ResultViewTrack(@NonNull final Style style, @NonNull final PaymentResult payment) {
        this.style = style;
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
        final Map<String, Object> data = super.getData();
        data.put(ATTR_STYLE, style.value);
        data.put(ATTR_PAYMENT_ID, payment.getPaymentId());
        data.put(ATTR_PAYMENT_STATUS, payment.getPaymentStatus());
        data.put(ATTR_PAYMENT_STATUS_DETAIL, payment.getPaymentStatusDetail());
        if (payment.getPaymentData() != null && payment.getPaymentData().getPaymentMethod() != null) {
            data.putAll(new FromPaymentMethodToAvailableMethods().map(payment.getPaymentData()
                .getPaymentMethod())
                .toMap());
        }
        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return String.format(Locale.US, PATH, getMappedResult(payment));
    }
}

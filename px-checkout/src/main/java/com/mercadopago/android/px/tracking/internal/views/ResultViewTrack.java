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

    private static final String ATTR_RAW_AMOUNT = "preference_amount";
    private static final String ATTR_PAYER_COST_TOTAL_AMOUNT = "payer_cost_total_amount";
    private static final String ATTR_CURRENCY_ID = "currency_id";
    private static final String ATTR_DISCOUNT_ID = "discount_id";
    private static final String ATTR_DISCOUNT_COUPON_AMOUNT = "discount_coupon_amount";
    private static final String ATTR_HAS_SPLIT = "has_split_payment";

    @NonNull private final Style style;
    @NonNull private final PaymentResult payment;
    @NonNull private final String currencyId;
    private final boolean hasSplitPayment;

    public enum Style {
        GENERIC("generic"),
        CUSTOM("custom");

        @NonNull public final String value;

        Style(@NonNull final String value) {
            this.value = value;
        }
    }

    public ResultViewTrack(@NonNull final Style style, @NonNull final PaymentResult payment,
        @NonNull final String currencyId, final boolean hasSplitPayment) {
        this.style = style;
        this.payment = payment;
        this.currencyId = currencyId;
        this.hasSplitPayment = hasSplitPayment;
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
        data.put(ATTR_CURRENCY_ID, currencyId);
        data.put(ATTR_HAS_SPLIT, hasSplitPayment);

        if (payment.getPaymentData() != null && payment.getPaymentData().getPaymentMethod() != null) {
            data.putAll(
                new FromPaymentMethodToAvailableMethods().map(payment.getPaymentData().getPaymentMethod()).toMap());

            if (payment.getPaymentData().getPayerCost() != null) {
                data.put(ATTR_PAYER_COST_TOTAL_AMOUNT, payment.getPaymentData().getPayerCost().getTotalAmount());
            }

            data.put(ATTR_RAW_AMOUNT, payment.getPaymentData().getRawAmount());

            if (payment.getPaymentData().getDiscount() != null) {
                data.put(ATTR_DISCOUNT_ID, payment.getPaymentData().getDiscount().getId());
                data.put(ATTR_DISCOUNT_COUPON_AMOUNT, payment.getPaymentData().getDiscount().getCouponAmount());
            }
        }

        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return String.format(Locale.US, PATH, getMappedResult(payment));
    }
}

package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.PaymentDataHelper;
import com.mercadopago.android.px.model.PaymentData;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.tracking.internal.mapper.FromPaymentMethodToAvailableMethods;
import com.mercadopago.android.px.tracking.internal.model.AvailableMethod;
import com.mercadopago.android.px.tracking.internal.model.TrackingMapModel;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
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
        @NonNull final String currencyId) {
        resultViewTrackModel = new ResultViewTrackModel(style, payment, currencyId, payment.getPaymentDataList());
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
        return resultViewTrackModel.toMap();
    }

    @NonNull
    @Override
    public String getViewPath() {
        return String.format(Locale.US, PATH, getMappedResult(payment));
    }

    private static final class ResultViewTrackModel extends TrackingMapModel {

        final String style;
        final Long paymentId;
        final String paymentStatus;
        final String paymentStatusDetail;
        final String currencyId;
        final boolean hasSplitPayment;
        BigDecimal rawAmount;
        BigDecimal discountCouponAmount;
        AvailableMethod availableMethod;

        ResultViewTrackModel(@NonNull final Style style, @NonNull final PaymentResult payment,
            @NonNull final String currencyId, @NonNull final Collection<PaymentData> paymentDataList) {
            this.style = style.value;
            paymentId = payment.getPaymentId();
            paymentStatus = payment.getPaymentStatus();
            paymentStatusDetail = payment.getPaymentStatusDetail();
            this.currencyId = currencyId;
            hasSplitPayment = PaymentDataHelper.isSplitPayment(paymentDataList);

            if (payment.getPaymentData() != null && payment.getPaymentData().getPaymentMethod() != null) {
                availableMethod =
                    new FromPaymentMethodToAvailableMethods().map(payment.getPaymentData().getPaymentMethod());
                rawAmount = payment.getPaymentData().getRawAmount();
                if (payment.getPaymentData().getDiscount() != null) {
                    discountCouponAmount = PaymentDataHelper.getTotalDiscountAmount(paymentDataList);
                }
            }
        }
    }
}

package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.PaymentDataHelper;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.internal.views.ResultViewTrack;
import java.math.BigDecimal;

public final class ResultViewTrackModel extends TrackingMapModel {

    private final String style;
    private String paymentMethodId;
    private String paymentMethodType;
    private final Long paymentId;
    private final String paymentStatus;
    private final String paymentStatusDetail;
    private final String currencyId;
    private final boolean hasSplitPayment;
    private final BigDecimal preferenceAmount;
    private final BigDecimal discountCouponAmount;

    public ResultViewTrackModel(@NonNull final ResultViewTrack.Style style, @NonNull final PaymentResult payment,
        @NonNull final CheckoutPreference checkoutPreference, @NonNull final String currencyId) {
        this.style = style.value;
        this.currencyId = currencyId;
        paymentId = payment.getPaymentId();
        paymentStatus = payment.getPaymentStatus();
        paymentStatusDetail = payment.getPaymentStatusDetail();
        hasSplitPayment = PaymentDataHelper.isSplitPayment(payment.getPaymentDataList());
        preferenceAmount = checkoutPreference.getTotalAmount();
        discountCouponAmount = PaymentDataHelper.getTotalDiscountAmount(payment.getPaymentDataList());

        if (payment.getPaymentData() != null && payment.getPaymentData().getPaymentMethod() != null) {
            paymentMethodId = payment.getPaymentData().getPaymentMethod().getId();
            paymentMethodType = payment.getPaymentData().getPaymentMethod().getPaymentTypeId();
        }
    }

    public Long getPaymentId() {
        return paymentId;
    }
}
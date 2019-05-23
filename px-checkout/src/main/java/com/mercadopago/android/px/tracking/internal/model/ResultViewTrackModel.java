package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.PaymentDataHelper;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.internal.mapper.FromPaymentMethodToAvailableMethods;
import com.mercadopago.android.px.tracking.internal.views.ResultViewTrack;
import java.math.BigDecimal;

public final class ResultViewTrackModel extends TrackingMapModel {

    private final String style;
    private final Long paymentId;
    private final String paymentStatus;
    private final String paymentStatusDetail;
    private final String currencyId;
    private final boolean hasSplitPayment;
    private final BigDecimal preferenceAmount;
    private final BigDecimal discountCouponAmount;
    private AvailableMethod availableMethod;

    public ResultViewTrackModel(@NonNull final ResultViewTrack.Style style, @NonNull final PaymentResult payment,
        @NonNull final CheckoutPreference checkoutPreference) {
        this.style = style.value;
        paymentId = payment.getPaymentId();
        paymentStatus = payment.getPaymentStatus();
        paymentStatusDetail = payment.getPaymentStatusDetail();
        currencyId = checkoutPreference.getSite().getCurrencyId();
        hasSplitPayment = PaymentDataHelper.isSplitPayment(payment.getPaymentDataList());
        preferenceAmount = checkoutPreference.getTotalAmount();
        discountCouponAmount = PaymentDataHelper.getTotalDiscountAmount(payment.getPaymentDataList());

        if (payment.getPaymentData() != null && payment.getPaymentData().getPaymentMethod() != null) {
            availableMethod =
                new FromPaymentMethodToAvailableMethods().map(payment.getPaymentData().getPaymentMethod());
        }
    }

    public Long getPaymentId() {
        return paymentId;
    }
}

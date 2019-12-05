package com.mercadopago.android.px.model.commission;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.core.DynamicDialogCreator;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import java.io.Serializable;
import java.math.BigDecimal;

public final class PaymentTypeChargeRule implements Serializable {

    @NonNull private final BigDecimal charge;

    @NonNull private final String paymentTypeId;

    @Nullable private final DynamicDialogCreator detailModal;

    @Nullable private final String message;

    /**
     * Factory method to create a charge free rule, used to highlight payment types without charges.
     * @param paymentTypeId payment type without charges
     * @param message message which will be shown in the highlighted payment type
     * @return
     */
    public static PaymentTypeChargeRule createChargeFreeRule(@NonNull final String paymentTypeId,
        @NonNull final String message) {
        return new PaymentTypeChargeRule(paymentTypeId, BigDecimal.ZERO, null, message);
    }

    /**
     * @param paymentTypeId the payment type associated with the charge to shouldBeTriggered.
     * @param charge the charge amount to apply for this rule
     */
    public PaymentTypeChargeRule(@NonNull final String paymentTypeId, @NonNull final BigDecimal charge) {
        this(paymentTypeId, charge, null);
    }

    /**
     * @param paymentTypeId the payment type associated with the charge to shouldBeTriggered.
     * @param charge the charge amount to apply for this rule
     * @param detailModal creator for the dialog with charge info
     */
    public PaymentTypeChargeRule(@NonNull final String paymentTypeId, @NonNull final BigDecimal charge,
        @Nullable final DynamicDialogCreator detailModal) {
        this(paymentTypeId, charge, detailModal, null);
    }

    private PaymentTypeChargeRule(@NonNull final String paymentTypeId, @NonNull final BigDecimal charge,
        @Nullable final DynamicDialogCreator detailModal, @Nullable final String message) {
        this.paymentTypeId = paymentTypeId;
        this.charge = charge;
        this.detailModal = detailModal;
        this.message = message;
    }

    //Shouldn't really exist
    @Deprecated
    public boolean shouldBeTriggered(@NonNull final ChargeRepository chargeRepository) {
        return false;
    }

    public boolean hasDetailModal() {
        return detailModal != null;
    }

    @NonNull
    public BigDecimal charge() {
        return charge;
    }

    @NonNull
    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    @Nullable
    public DynamicDialogCreator getDetailModal() {
        return detailModal;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
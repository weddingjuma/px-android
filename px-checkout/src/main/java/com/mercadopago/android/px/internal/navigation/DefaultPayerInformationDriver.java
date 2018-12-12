package com.mercadopago.android.px.internal.navigation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.Identification;
import com.mercadopago.android.px.model.Payer;
import com.mercadopago.android.px.model.PaymentMethod;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class DefaultPayerInformationDriver {
    @Nullable private final Payer payer;
    @NonNull private final PaymentMethod selectedPaymentMethod;
    private static final String ADDITIONAL_INFO_NAME = "name";
    private static final String ADDITIONAL_INFO_IDENTIFICATION_TYPE = "identification_type";
    private static final String ADDITIONAL_INFO_IDENTIFICATION_NUMBER = "identification_number";

    public DefaultPayerInformationDriver(@Nullable final Payer payer,
        @NonNull final PaymentMethod selectedPaymentMethod) {
        this.payer = payer;
        this.selectedPaymentMethod = selectedPaymentMethod;
    }

    public void drive(@NonNull final PayerInformationDriverCallback callback) {
        if (resolveAdditionalInfo(selectedPaymentMethod)) {
            if (isPayerInformationValid(payer)) {
                callback.driveToReviewConfirm();
            } else {
                callback.driveToNewPayerData();
            }
        } else {
            callback.driveToReviewConfirm();
        }
    }

    private boolean isPayerInformationValid(@Nullable final Payer payer) {
        return payer != null
            && !(!isIdentificationValid(payer.getIdentification())
            || StringUtils.isEmpty(payer.getFirstName())
            || StringUtils.isEmpty(payer.getLastName()));
    }

    private boolean isIdentificationValid(final Identification identification) {
        return identification != null
            && !StringUtils.isEmpty(identification.getNumber())
            && !StringUtils.isEmpty(identification.getType());
    }

    private boolean resolveAdditionalInfo(@NonNull final PaymentMethod selectedPaymentMethod) {
        final String paymentMethodId = selectedPaymentMethod.getId();
        final List<String> additionalInfoNeeded = selectedPaymentMethod.getAdditionalInfoNeeded();
        return additionalInfoNeeded != null && (additionalInfoNeeded.contains(paymentMethodId + "_" + ADDITIONAL_INFO_NAME)
            || additionalInfoNeeded.contains(paymentMethodId + "_" + ADDITIONAL_INFO_IDENTIFICATION_TYPE)
            || additionalInfoNeeded.contains(paymentMethodId + "_" + ADDITIONAL_INFO_IDENTIFICATION_NUMBER));
    }

    public interface PayerInformationDriverCallback {
        void driveToNewPayerData();

        void driveToReviewConfirm();
    }
}
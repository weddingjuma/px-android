package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.configuration.PaymentResultScreenConfiguration;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.viewmodel.BusinessPaymentModel;
import com.mercadopago.android.px.internal.viewmodel.PaymentModel;
import com.mercadopago.android.px.model.PaymentResult;
import com.mercadopago.android.px.tracking.internal.model.ResultViewTrackModel;
import java.util.Collections;
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
    private final String paymentStatus;
    private final List<String> remedyList;

    public ResultViewTrack(@NonNull final PaymentModel paymentModel,
        @NonNull final PaymentResultScreenConfiguration screenConfiguration,
        @NonNull final PaymentSettingRepository paymentSetting, @NonNull final List<String> remedyList) {
        resultViewTrackModel =
            new ResultViewTrackModel(paymentModel, screenConfiguration, paymentSetting.getCheckoutPreference(),
                paymentSetting.getCurrency().getId());
        paymentStatus = getMappedResult(paymentModel.getPaymentResult());
        this.remedyList = remedyList;
    }

    public ResultViewTrack(@NonNull final BusinessPaymentModel paymentModel,
        @NonNull final PaymentSettingRepository paymentSetting) {
        resultViewTrackModel = new ResultViewTrackModel(paymentModel, paymentSetting.getCheckoutPreference(),
            paymentSetting.getCurrency().getId());
        paymentStatus = getMappedResult(paymentModel.getPaymentResult());
        remedyList = Collections.emptyList();
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
        Map<String, Object> map = resultViewTrackModel.toMap();
        if (paymentStatus.equals(ERROR)) {
            map.put("recoverable", !remedyList.isEmpty());
            map.put("remedies", remedyList);
        }
        return map;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return String.format(Locale.US, PATH, paymentStatus);
    }
}
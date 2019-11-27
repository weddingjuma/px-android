package com.mercadopago.android.px.internal.features.disable_payment_method;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import com.mercadolibre.android.ui.widgets.MeliDialog;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.StatusMetadata;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import com.mercadopago.android.px.tracking.internal.views.DisabledPaymentMethodDetailViewTracker;

public class DisabledPaymentMethodDetailDialog extends MeliDialog {

    private static final String TAG = DisabledPaymentMethodDetailDialog.class.getName();
    private static final String ARG_DISABLED_PAYMENT_METHOD = "arg_disabled_payment_method";
    private static final String ARG_STATUS_METADATA = "arg_status_metadata";

    private DisabledPaymentMethod disabledPaymentMethod;
    private StatusMetadata status;

    public static void showDialog(@NonNull final Fragment targetFragment, final int requestCode,
        @NonNull final DisabledPaymentMethod disabledPaymentMethod,
        final StatusMetadata status) {
        final DisabledPaymentMethodDetailDialog disabledPaymentMethodDetailDialog =
            showDialog(targetFragment.getActivity().getSupportFragmentManager(), disabledPaymentMethod, status);
        disabledPaymentMethodDetailDialog.setTargetFragment(targetFragment, requestCode);
    }

    public static DisabledPaymentMethodDetailDialog showDialog(@NonNull final FragmentManager supportFragmentManager,
        @NonNull final DisabledPaymentMethod disabledPaymentMethod, final StatusMetadata status) {
        final DisabledPaymentMethodDetailDialog instance = new DisabledPaymentMethodDetailDialog();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_DISABLED_PAYMENT_METHOD, disabledPaymentMethod);
        arguments.putParcelable(ARG_STATUS_METADATA, status);
        instance.setArguments(arguments);
        instance.show(supportFragmentManager, TAG);
        return instance;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_DISABLED_PAYMENT_METHOD) &&
            arguments.containsKey(ARG_STATUS_METADATA)) {
            //noinspection ConstantConditions
            disabledPaymentMethod = getArguments().getParcelable(ARG_DISABLED_PAYMENT_METHOD);
            status = getArguments().getParcelable(ARG_STATUS_METADATA);
        } else {
            throw new IllegalStateException(getClass().getSimpleName() + " does not contain model info");
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View closeButton = view.findViewById(R.id.ui_melidialog_close_button);
        closeButton.setOnClickListener(v -> dismiss());
        final TextView content = view.findViewById(R.id.px_dialog_detail_payment_method_disable_content);

        new DisabledPaymentMethodDetailViewTracker().track();

        if (status != null && !status.isEnabled()) {
            content.setText(status.getSecondaryMessage().getMessage());
        } else {
            content.setText(getTitleResourceId(disabledPaymentMethod.getPaymentStatusDetail()));
        }
        final View linkText = view.findViewById(R.id.px_dialog_detail_payment_method_disable_link);
        linkText.setOnClickListener(v -> finish());
    }

    public void finish() {
        dismiss();
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.px_dialog_detail_payment_method_disable;
    }

    @StringRes
    private static int getTitleResourceId(final String statusDetail) {
        switch (statusDetail) {
        case Payment.StatusDetail
            .STATUS_DETAIL_REJECTED_HIGH_RISK:
        case Payment.StatusDetail
            .STATUS_DETAIL_CC_REJECTED_HIGH_RISK:
            return R.string.px_dialog_detail_payment_method_disable_high_risk;
        case Payment.StatusDetail
            .STATUS_DETAIL_CC_REJECTED_BLACKLIST:
            return R.string.px_dialog_detail_payment_method_disable_black_list;
        case Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT:
            return R.string.px_dialog_detail_payment_method_disable_insufficient_amount;
        default:
            return 0;
        }
    }
}
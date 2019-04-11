package com.mercadopago.android.px.internal.features.express.slider;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.disable_payment_method.DisabledPaymentMethodDetailDialog;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;

public abstract class PaymentMethodFragment extends Fragment implements PaymentMethod.View {

    protected static final String ARG_MODEL = "ARG_MODEL";
    protected static final String ARG_PM_TYPE = "ARG_PM_TYPE";

    private View badge;
    private CardView card;
    private String paymentMethodType;
    private PaymentMethodPresenter presenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PaymentMethodPresenter(
            Session.getSession(getContext()).getConfigurationModule().getDisabledPaymentMethodRepository(),
            (DrawableFragmentItem) getArguments().getSerializable(ARG_MODEL));
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        badge = view.findViewById(R.id.px_disabled_badge);
        card = view.findViewById(R.id.payment_method);
        if (arguments != null && arguments.containsKey(ARG_MODEL)
            && arguments.containsKey(ARG_PM_TYPE)) {
            paymentMethodType = arguments.getString(ARG_PM_TYPE);
        } else {
            throw new IllegalStateException("PaymentMethodFragment does not contain model info");
        }
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }

    @Override
    public void disable() {
        badge.setVisibility(View.VISIBLE);
        ViewUtils.grayScaleViewGroup(card);
        card.setOnClickListener(
            v -> DisabledPaymentMethodDetailDialog.showDialog(getChildFragmentManager(), paymentMethodType));
    }
}
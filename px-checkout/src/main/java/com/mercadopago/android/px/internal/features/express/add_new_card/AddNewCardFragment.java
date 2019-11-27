package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.payment_vault.PaymentVaultActivity;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;

public class AddNewCardFragment extends BaseFragment<AddNewCardPresenter, AddNewCardFragmentDrawableFragmentItem>
    implements AddNewCard.View, View.OnClickListener {

    @NonNull
    public static Fragment getInstance(@NonNull final AddNewCardFragmentDrawableFragmentItem model) {
        final AddNewCardFragment instance = new AddNewCardFragment();
        instance.storeModel(model);
        return instance;
    }

    protected AddNewCardPresenter createPresenter() {
        return new AddNewCardPresenter(Session.getInstance().getInitRepository());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_change_payment_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureClick(view);
    }

    @Override
    public void showPaymentMethods() {
        //TODO refactor
        PaymentVaultActivity.start(getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT);
    }

    @Override
    public void showPaymentMethodsWithSelection(@NonNull final PaymentMethodSearchItem paymentMethodSearchItem) {
        //TODO refactor
        PaymentVaultActivity
            .startWithPaymentMethodSelected(getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT, paymentMethodSearchItem);
    }

    protected void configureClick(@NonNull final View view) {
        final FloatingActionButton floating = view.findViewById(R.id.floating_change);
        final MeliButton message = view.findViewById(R.id.message);

        message.setText(model.metadata.getLabel().getMessage());
        floating.setScaleType(ImageView.ScaleType.CENTER);
        floating.setOnClickListener(this);
        message.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        presenter.onAddNewCardSelected();
    }
}
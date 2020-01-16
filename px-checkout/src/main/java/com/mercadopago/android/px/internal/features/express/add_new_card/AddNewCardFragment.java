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
import com.mercadolibre.android.cardform.CardForm;
import com.mercadolibre.android.cardform.internal.CardFormWithFragment;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.express.ExpressPaymentFragment;
import com.mercadopago.android.px.internal.features.payment_vault.PaymentVaultActivity;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;

public class AddNewCardFragment extends BaseFragment<AddNewCardPresenter, AddNewCardFragmentDrawableFragmentItem>
    implements AddNewCard.View, View.OnClickListener {

    private static final String OLD_VERSION = "v11111";

    @NonNull
    public static Fragment getInstance(@NonNull final AddNewCardFragmentDrawableFragmentItem model) {
        final AddNewCardFragment instance = new AddNewCardFragment();
        instance.storeModel(model);
        return instance;
    }

    @Override
    protected AddNewCardPresenter createPresenter() {
        if (OLD_VERSION.equals(model.metadata.getVersion())) {
            return new AddNewCardOldPresenter(Session.getInstance().getInitRepository());
        } else {
            return new AddNewCardPresenter(Session.getInstance().getConfigurationModule().getPaymentSettings());
        }
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
    public void startCardForm(@NonNull final CardFormWithFragment cardForm) {
        cardForm.start(getParentFragment().getFragmentManager(), ExpressPaymentFragment.REQ_CODE_CARD_FORM,
            R.id.one_tap_fragment);
    }

    @Override
    public void showPaymentMethods(@Nullable final PaymentMethodSearchItem paymentMethodSearchItem) {
        if (paymentMethodSearchItem == null) {
            PaymentVaultActivity.start(getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT);
        } else {
            PaymentVaultActivity.startWithPaymentMethodSelected(
                getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT, paymentMethodSearchItem);
        }
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
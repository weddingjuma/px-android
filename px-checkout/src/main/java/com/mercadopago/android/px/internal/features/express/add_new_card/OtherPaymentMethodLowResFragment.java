package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.viewmodel.drawables.OtherPaymentMethodFragmentItem;

public class OtherPaymentMethodLowResFragment extends OtherPaymentMethodFragment {

    @NonNull
    public static Fragment getInstance(@NonNull final OtherPaymentMethodFragmentItem model) {
        final OtherPaymentMethodLowResFragment instance = new OtherPaymentMethodLowResFragment();
        instance.storeModel(model);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_other_payment_method_low_res, container, false);
    }

}
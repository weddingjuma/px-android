package com.mercadopago.android.px.internal.features.express.slider;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.view.LinkableTextView;
import com.mercadopago.android.px.internal.viewmodel.drawables.ConsumerCreditsDrawableFragmentItem;
import com.mercadopago.android.px.model.ConsumerCreditsDisplayInfo;
import com.mercadopago.android.px.model.PaymentTypes;

public class ConsumerCreditsFragment extends PaymentMethodFragment {

    @NonNull
    public static Fragment getInstance(final ConsumerCreditsDrawableFragmentItem drawableCard) {
        final ConsumerCreditsFragment savedCardFragment = new ConsumerCreditsFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MODEL, drawableCard);
        bundle.putString(ARG_PM_TYPE, PaymentTypes.DIGITAL_CURRENCY);
        savedCardFragment.setArguments(bundle);
        return savedCardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_consumer_credits, container, false);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ConsumerCreditsDrawableFragmentItem drawableCard = getArguments().getParcelable(ARG_MODEL);
        final ConsumerCreditsDisplayInfo displayInfo = drawableCard.metadata.displayInfo;
        tintBackground(view.findViewById(R.id.background), displayInfo.color);
        showDisplayInfo(view, displayInfo);
    }

    protected void showDisplayInfo(final View view, @NonNull final ConsumerCreditsDisplayInfo displayInfo) {
        ((LinkableTextView) view.findViewById(R.id.top_text)).updateModel(displayInfo.topText);
        ((LinkableTextView) view.findViewById(R.id.bottom_text)).updateModel(displayInfo.bottomText);
    }
}
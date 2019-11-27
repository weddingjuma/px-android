package com.mercadopago.android.px.internal.features.express.slider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.LinkableTextView;
import com.mercadopago.android.px.internal.viewmodel.DisableConfiguration;
import com.mercadopago.android.px.internal.viewmodel.drawables.ConsumerCreditsDrawableFragmentItem;
import com.mercadopago.android.px.model.ConsumerCreditsDisplayInfo;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;

public class ConsumerCreditsFragment extends PaymentMethodFragment<ConsumerCreditsDrawableFragmentItem> {

    private ConstraintLayout creditsLagout;
    private ImageView background;
    private ImageView logo;
    private LinkableTextView topText;
    private LinkableTextView bottomText;

    @NonNull
    public static Fragment getInstance(final ConsumerCreditsDrawableFragmentItem model) {
        final ConsumerCreditsFragment instance = new ConsumerCreditsFragment();
        instance.storeModel(model);
        return instance;
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
        creditsLagout = view.findViewById(R.id.credits_layout);
        background = view.findViewById(R.id.background);
        logo = view.findViewById(R.id.logo);
        topText = view.findViewById(R.id.top_text);
        bottomText = view.findViewById(R.id.bottom_text);
        final ConsumerCreditsDisplayInfo displayInfo = model.metadata.displayInfo;
        tintBackground(view.findViewById(R.id.background), displayInfo.color);
        showDisplayInfo(view, displayInfo);
    }

    protected void showDisplayInfo(final View view, @NonNull final ConsumerCreditsDisplayInfo displayInfo) {
        topText.updateModel(displayInfo.topText);
        bottomText.updateModel(displayInfo.bottomText);
    }

    @Override
    public void disable(@NonNull final DisabledPaymentMethod disabledPaymentMethod) {
        super.disable(disabledPaymentMethod);
        final DisableConfiguration disableConfiguration = new DisableConfiguration(getContext());
        ViewUtils.grayScaleViewGroup(creditsLagout);
        background.clearColorFilter();
        background.setImageResource(0);
        background.setBackgroundColor(disableConfiguration.getBackgroundColor());
        topText.setVisibility(View.GONE);
        bottomText.setVisibility(View.GONE);
        centerLogo();
    }

    private void centerLogo() {
        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(creditsLagout);
        constraintSet.connect(logo.getId(), ConstraintSet.LEFT, creditsLagout.getId(), ConstraintSet.LEFT, 0);
        constraintSet.connect(logo.getId(), ConstraintSet.RIGHT, creditsLagout.getId(), ConstraintSet.RIGHT, 0);
        constraintSet.connect(logo.getId(), ConstraintSet.TOP, creditsLagout.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(logo.getId(), ConstraintSet.BOTTOM, creditsLagout.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.applyTo(creditsLagout);
    }
}
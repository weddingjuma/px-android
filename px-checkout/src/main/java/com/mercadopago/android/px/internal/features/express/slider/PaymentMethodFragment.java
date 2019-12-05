package com.mercadopago.android.px.internal.features.express.slider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.disable_payment_method.DisabledPaymentMethodDetailDialog;
import com.mercadopago.android.px.internal.features.express.animations.BottomSlideAnimationSet;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import java.util.Arrays;

public abstract class PaymentMethodFragment<T extends DrawableFragmentItem>
    extends BaseFragment<PaymentMethodPresenter, T> implements PaymentMethod.View, Focusable {

    private CardView card;
    private BottomSlideAnimationSet animation;
    private boolean focused;

    @Override
    protected PaymentMethodPresenter createPresenter() {
        return new PaymentMethodPresenter(
            Session.getInstance().getConfigurationModule().getDisabledPaymentMethodRepository(), model);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        animation = new BottomSlideAnimationSet();
    }

    @Override
    public void onDetach() {
        animation = null;
        super.onDetach();
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        card = view.findViewById(R.id.payment_method);
        final View highlightContainer = view.findViewById(R.id.highlight_container);
        final MPTextView highlightText = view.findViewById(R.id.highlight_text);
        highlightText.setText(model.getHighlightMessage());
        animation.initialize(Arrays.asList(highlightContainer, highlightText));
        if (hasFocus()) {
            onFocusIn();
        }
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onFocusIn();
        } else {
            onFocusOut();
        }
    }

    @Override
    public void onFocusIn() {
        focused = true;
        if (hasToHighlight()) {
            animation.slideUp();
        }
    }

    @Override
    public void onFocusOut() {
        focused = false;
        if (hasToHighlight()) {
            animation.slideDown();
        }
    }

    @Override
    public boolean hasFocus() {
        return focused;
    }

    private boolean hasToHighlight() {
        return animation != null && TextUtil.isNotEmpty(model.getHighlightMessage());
    }

    @Override
    public void disable(@NonNull final DisabledPaymentMethod disabledPaymentMethod) {
        final Fragment parentFragment = getParentFragment();
        if (!(parentFragment instanceof DisabledDetailDialogLauncher)) {
            throw new IllegalStateException(
                "Parent fragment should implement " + DisabledDetailDialogLauncher.class.getSimpleName());
        }
        card.setOnClickListener(
            v -> DisabledPaymentMethodDetailDialog
                .showDialog(parentFragment, ((DisabledDetailDialogLauncher) parentFragment).getRequestCode(),
                    disabledPaymentMethod, model.getStatus()));
    }

    protected void tintBackground(@NonNull final ImageView background, @NonNull final String color) {
        final int backgroundColor = Color.parseColor(color);

        final int alpha = Color.alpha(backgroundColor);
        final int blue = Color.blue(backgroundColor);
        final int green = Color.green(backgroundColor);
        final int red = Color.red(backgroundColor);

        final int lighterBackgroundColor =
            Color.argb((int) (alpha * 0.7f), (int) (red * 0.8f), (int) (green * 0.8f), (int) (blue * 0.8f));
        Color.argb(0, 0, 0, 0);
        final int[] ints = { backgroundColor, lighterBackgroundColor };
        final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
            ints);

        gradientDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.px_xs_margin));
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setDither(true);

        background.setImageDrawable(gradientDrawable);
    }

    public interface DisabledDetailDialogLauncher {
        int getRequestCode();
    }
}
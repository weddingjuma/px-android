package com.mercadopago.android.px.internal.features.express.animations;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;

public class HideViewOnAnimationListener implements Animation.AnimationListener {

    private final View viewToHide;

    public HideViewOnAnimationListener(@NonNull final View viewToHide) {
        this.viewToHide = viewToHide;
    }

    @Override
    public void onAnimationStart(final Animation animation) {
        // Do nothing.
    }

    @Override
    public void onAnimationEnd(final Animation animation) {
        viewToHide.setVisibility(View.INVISIBLE);
        viewToHide.clearAnimation();
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        // Do nothing.
    }
}

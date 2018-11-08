package com.mercadopago.android.px.internal.features.express.animations;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import com.mercadopago.android.px.R;

public class SlideAnim {

    public void slideDown(@NonNull final View viewToAnimate, final float initialPosition,
        final float endPosition) {
        final ObjectAnimator slideDownAnim =
            ObjectAnimator.ofFloat(viewToAnimate, "y", initialPosition, endPosition);
        slideDownAnim
            .setDuration((long) viewToAnimate.getContext().getResources().getInteger(R.integer.px_default_animation_time));
        slideDownAnim.start();
    }

    public void slideUp(@NonNull final View viewToAnimate, final float initialPosition,
        final float endPosition) {
        final ObjectAnimator slideUpAnim =
            ObjectAnimator.ofFloat(viewToAnimate, "y", initialPosition, endPosition);
        slideUpAnim.setDuration(
            (long) viewToAnimate.getContext().getResources().getInteger(R.integer.px_default_animation_time));
        slideUpAnim.start();
    }
}

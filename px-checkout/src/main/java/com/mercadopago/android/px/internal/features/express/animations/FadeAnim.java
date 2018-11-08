package com.mercadopago.android.px.internal.features.express.animations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import com.mercadopago.android.px.R;

public class FadeAnim {

    private final Animation fadeInAnimation;
    private final Animation fadeOutAnimation;
    private final int normalDuration;
    private final int fastDuration;
    private final int fastestDuration;

    public FadeAnim(@NonNull final Context context) {
        fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setInterpolator(new LinearInterpolator());
        fadeInAnimation.setFillAfter(true);
        fadeOutAnimation = new AlphaAnimation(1, 0);
        fadeOutAnimation.setInterpolator(new AccelerateInterpolator());
        fadeOutAnimation.setFillAfter(true);
        normalDuration = context.getResources().getInteger(R.integer.px_long_animation_time);
        fastDuration = context.getResources().getInteger(R.integer.px_short_animation_time);
        fastestDuration = context.getResources().getInteger(R.integer.px_shortest_animation_time);
    }

    public void fadeIn(@NonNull final View viewToAnimate) {
        fadeInAnimation.setDuration(normalDuration);
        viewToAnimate.startAnimation(fadeInAnimation);
    }

    public void fadeInFastest(@NonNull final View viewToAnimate) {
        fadeInAnimation.setDuration(fastestDuration);
        viewToAnimate.startAnimation(fadeInAnimation);
    }

    public void fadeOut(@NonNull final View viewToAnimate) {
        fadeOutAnimation.setDuration(normalDuration);
        viewToAnimate.startAnimation(fadeOutAnimation);
    }

    public void fadeOutFast(@NonNull final View viewToAnimate) {
        fadeOutAnimation.setDuration(fastDuration);
        viewToAnimate.startAnimation(fadeOutAnimation);
    }
}

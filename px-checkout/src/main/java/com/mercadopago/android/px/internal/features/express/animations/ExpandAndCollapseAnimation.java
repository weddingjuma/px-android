package com.mercadopago.android.px.internal.features.express.animations;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.mercadopago.android.px.R;

import static android.view.View.VISIBLE;
import static com.mercadopago.android.px.internal.util.ViewUtils.cancelAnimation;
import static com.mercadopago.android.px.internal.util.ViewUtils.hasEndedAnim;

public class ExpandAndCollapseAnimation {

    private final Animation expandAnimation;
    private final Animation collapseAnimation;
    private final View targetView;

    public ExpandAndCollapseAnimation(@NonNull final View view) {
        targetView = view;
        expandAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.px_anim_expand);
        collapseAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.px_anim_collapse);
        collapseAnimation.setAnimationListener(new HideViewOnAnimationListener(view));
    }

    /**
     * Expand the view is not animating
     */
    public void expand() {
        if (hasEndedAnim(targetView)) {
            targetView.setAnimation(expandAnimation);
            targetView.startAnimation(expandAnimation);
        } else {
            cancelAnimation(targetView);
        }

        targetView.setVisibility(VISIBLE);
    }

    /**
     * Collapse the view is not animating
     */
    public void collapse() {
        if (hasEndedAnim(targetView)) {
            targetView.setAnimation(collapseAnimation);
            targetView.startAnimation(collapseAnimation);
        } else {
            cancelAnimation(targetView);
            targetView.setVisibility(View.GONE);
        }
    }
}

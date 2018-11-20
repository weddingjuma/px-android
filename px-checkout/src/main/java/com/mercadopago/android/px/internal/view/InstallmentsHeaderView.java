package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;
import java.util.ArrayList;
import java.util.List;

public class InstallmentsHeaderView extends LinearLayout {

    /* default */ List<InstallmentsDescriptorView> descriptorViews;
    /* default */ MPTextView installmentsTitleView;
    /* default */ ImageView arrow;
    /* default */ Animation rotateUp;
    /* default */ Animation rotateDown;

    private TitlePager titlePager;
    private List<InstallmentsDescriptorView.Model> installmentModels;
    private int currentIndex;

    public void setInstallmentsModel(final List<InstallmentsDescriptorView.Model> installmentModels) {
        this.installmentModels = installmentModels;
        titlePager.setModels(installmentModels);
    }

    public interface Listener {

        void onDescriptorViewClicked();

        void onInstallmentsSelectorCancelClicked();
    }

    public InstallmentsHeaderView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstallmentsHeaderView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.px_view_installments_header, this);

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.px_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.px_rotate_down);
        installmentsTitleView = findViewById(R.id.installments_title);
        titlePager = findViewById(R.id.title_pager);
        descriptorViews = new ArrayList<>();
        for (int i = 0; i < titlePager.getChildCount(); i++) {
            descriptorViews.add((InstallmentsDescriptorView) titlePager.getChildAt(i));
        }
        arrow = findViewById(R.id.arrow);
    }

    public void setListener(final Listener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (installmentsTitleView.getVisibility() == VISIBLE) {
                    arrow.startAnimation(rotateDown);
                    listener.onInstallmentsSelectorCancelClicked();
                } else {
                    arrow.startAnimation(rotateUp);
                    listener.onDescriptorViewClicked();
                }
            }
        });
    }

    public void update() {
        installmentsTitleView.setVisibility(VISIBLE);
        titlePager.setVisibility(GONE);
    }

    public void update(final int paymentMethodIndex, final int payerCostSelected) {
        currentIndex = paymentMethodIndex;

        if (installmentsTitleView.getVisibility() == VISIBLE) {
            arrow.startAnimation(rotateDown);
        }

        titlePager.setVisibility(VISIBLE);
        titlePager.orderViews(paymentMethodIndex);
        titlePager.updateData(paymentMethodIndex, payerCostSelected);
        installmentsTitleView.setVisibility(GONE);

        setClickable(installmentModels.get(currentIndex).hasPayerCostList());
    }

    public void updatePosition(final float positionOffset, final int position) {
        titlePager.updatePosition(positionOffset, position);
        fadeBasedOnPosition(positionOffset, position);
    }

    private void fadeBasedOnPosition(final float positionOffset, final int position) {

        float relativeOffset = positionOffset;

        final GoingTo goingTo = position == currentIndex ? GoingTo.FORWARD : GoingTo.BACKWARDS;

        final InstallmentsDescriptorView.Model currentModel = installmentModels.get(currentIndex);
        InstallmentsDescriptorView.Model goingToModel = null;
        if (GoingTo.BACKWARDS == goingTo && position >= 0) {
            goingToModel = installmentModels.get(position);
            relativeOffset = 1.0f - positionOffset;
        } else if (GoingTo.FORWARD == goingTo && position + 1 < installmentModels.size()) {
            goingToModel = installmentModels.get(position + 1);
        }

        if (currentModel.hasPayerCostList()) {
            if (goingToModel != null && !goingToModel.hasPayerCostList()) {
                arrow.setAlpha(1.0f - relativeOffset);
            } else {
                arrow.setAlpha(1.0f);
            }
        } else {
            if (goingToModel != null && goingToModel.hasPayerCostList()) {
                arrow.setAlpha(relativeOffset);
            } else {
                arrow.setAlpha(0.0f);
            }
        }
    }

    private enum GoingTo {
        FORWARD, BACKWARDS
    }
}
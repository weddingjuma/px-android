package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.mercadopago.android.px.R;
import java.util.List;

import static com.mercadopago.android.px.internal.view.InstallmentsDescriptorView.Model.SELECTED_PAYER_COST_NONE;

public class TitlePager extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private InstallmentsDescriptorView previousView;
    private InstallmentsDescriptorView currentView;
    private InstallmentsDescriptorView nextView;
    private int currentWidth;
    private int currentIndex = 0;

    private List<InstallmentsDescriptorView.Model> models;

    public TitlePager(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitlePager(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.px_view_title_pager, this);
    }

    public void setModels(final List<InstallmentsDescriptorView.Model> models) {
        this.models = models;
        refreshData(currentIndex, SELECTED_PAYER_COST_NONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        previousView = (InstallmentsDescriptorView) getChildAt(0);
        currentView = (InstallmentsDescriptorView) getChildAt(1);
        nextView = (InstallmentsDescriptorView) getChildAt(2);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (getWidth() > 0) {
            currentWidth = getWidth();
            resetViewsPosition();
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void updatePosition(final float offset, final int position) {
        float positionOffset = offset;

        if (positionOffset == 0.0f) {
            return;
        }

        if (position < currentIndex) {
            //Going backwards
            previousView.setAlpha(1.0f - positionOffset);
            currentView.setAlpha(positionOffset);
            positionOffset = 1.0f - positionOffset;
            positionOffset *= -1.0f;
        } else {
            //Added to fix lint error, not really needed
            positionOffset = Math.abs(positionOffset);
            nextView.setAlpha(positionOffset);
            currentView.setAlpha(1.0f - positionOffset);
        }

        final float offsetInPixels = currentWidth * positionOffset;

        previousView.setX(-currentWidth - offsetInPixels);
        currentView.setX(-offsetInPixels);
        nextView.setX(currentWidth - offsetInPixels);
    }

    public void orderViews(final int position) {
        final InstallmentsDescriptorView auxView;
        if (currentIndex < position) {
            auxView = previousView;
            previousView = currentView;
            currentView = nextView;
            nextView = auxView;
        } else if (currentIndex > position) {
            auxView = nextView;
            nextView = currentView;
            currentView = previousView;
            previousView = auxView;
        }
    }

    public void updateData(final int currentIndex, final int payerCostSelected) {
        this.currentIndex = currentIndex;
        refreshData(currentIndex, payerCostSelected);
    }

    private void refreshData(final int currentIndex, final int payerCostSelected) {
        if (currentIndex > 0) {
            final InstallmentsDescriptorView.Model previousModel = models.get(currentIndex - 1);
            previousView.update(previousModel);
        }

        final InstallmentsDescriptorView.Model currentModel = models.get(currentIndex);
        currentModel.setCurrentPayerCost(payerCostSelected);
        currentView.update(currentModel);

        if (currentIndex + 1 < models.size()) {
            final InstallmentsDescriptorView.Model nextModel = models.get(currentIndex + 1);
            nextView.update(nextModel);
        }
    }

    private void resetViewsPosition() {
        currentView.setX(0);
        previousView.setX(-currentWidth);
        nextView.setX(currentWidth);
    }
}

package com.mercadopago.android.px.internal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.mercadopago.android.px.R;

public class FixedAspectRatioFrameLayout extends FrameLayout {
    private int aspectRatioWidth;
    private int aspectRatioHeight;

    public FixedAspectRatioFrameLayout(final Context context) {
        super(context);
    }

    public FixedAspectRatioFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FixedAspectRatioFrameLayout(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PXFixedAspectRatioFrameLayout);
        aspectRatioWidth = a.getInt(R.styleable.PXFixedAspectRatioFrameLayout_px_aspectRatioWidth, 4);
        aspectRatioHeight = a.getInt(R.styleable.PXFixedAspectRatioFrameLayout_px_aspectRatioHeight, 3);
        a.recycle();
    }

    public void setAspectRatio(final int aspectRatioWidth, final int aspectRatioHeight) {
        this.aspectRatioWidth = aspectRatioWidth;
        this.aspectRatioHeight = aspectRatioHeight;
        invalidate();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int originalWidth = MeasureSpec.getSize(widthMeasureSpec);

        final int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int calculatedHeight = originalWidth * aspectRatioHeight / aspectRatioWidth;

        final int finalWidth;
        final int finalHeight;

        if (calculatedHeight > originalHeight) {
            finalWidth = originalHeight * aspectRatioWidth / aspectRatioHeight;
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
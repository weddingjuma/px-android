package com.mercadopago.android.px.internal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import com.mercadopago.android.px.R;

public class DynamicTextViewRowView extends LinearLayout {

    public static final String SPACE = " ";
    private int fontColor;

    public DynamicTextViewRowView(final Context context) {
        this(context, null);
    }

    public DynamicTextViewRowView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicTextViewRowView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        setOrientation(LinearLayout.HORIZONTAL);
        fontColor = getResources().getColor(R.color.white);
    }

    public void setColor(final int fontColor) {
        this.fontColor = fontColor;
    }

    public void setText(@NonNull final String text, @NonNull final String regex) {
        final String[] blocks = text.split(regex);
        int blocksCount = 0;

        for (final String block : blocks) {
            addCardNumberBlockTextView(block);

            if (blocksCount != blocks.length - 1) {
                addSpace();
            }

            blocksCount++;
        }
    }

    @SuppressLint("RestrictedApi")
    private void addCardNumberBlockTextView(@NonNull final String blockText) {
        final int weight = 1;
        final int maxLines = 1;
        final int maxLength = 40;
        final int shadowDy = 2;
        final int shadowDx = 2;
        final int shadowRadius = 2;
        final int autoSizeMaxTextSize = 24;
        final int autoSizeMinTextSize = 5;
        final int autoSizeStepGranularity = 1;

        final MPTextView mpTextView = new MPTextView(getContext());

        final LayoutParams params =
            new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = weight;
        mpTextView.setLayoutParams(params);
        mpTextView.setGravity(Gravity.CENTER);
        mpTextView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
        mpTextView.setShadowLayer(shadowRadius, shadowDx, shadowDy, getResources().getColor(R.color.px_base_text));

        mpTextView.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize,
            autoSizeStepGranularity, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        mpTextView.setMaxLines(maxLines);
        mpTextView.setFontStyle(MPTextView.MONO_REGULAR);
        mpTextView.setTextColor(fontColor);
        mpTextView.setText(blockText);
        mpTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.px_l_text));
        addView(mpTextView);
    }

    private void addSpace() {
        final Space space = new Space(getContext());
        final LayoutParams params =
            new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        space.setLayoutParams(params);
        addView(space);
    }
}

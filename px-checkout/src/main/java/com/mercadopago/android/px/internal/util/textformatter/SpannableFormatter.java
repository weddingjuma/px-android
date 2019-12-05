package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.ViewUtils;

public class SpannableFormatter extends ChainFormatter {
    private static final String SEPARATOR = " ";
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private PxFont font;
    private boolean hasSpace;

    public SpannableFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        font = PxFont.REGULAR;
    }

    public SpannableFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public SpannableFormatter withStyle(@NonNull final String name) {
        font = PxFont.from(name);
        return this;
    }

    public SpannableFormatter withStyle(@NonNull final PxFont pxFont) {
        font = pxFont;
        return this;
    }

    public SpannableFormatter withSpace() {
        hasSpace = true;
        return this;
    }

    public Spannable apply(@StringRes final int resId) {
        return apply(context.getString(resId));
    }

    @Override
    public Spannable apply(final CharSequence text) {
        final int indexStart = spannableStringBuilder.length();

        if (hasSpace) {
            spannableStringBuilder.append(SEPARATOR);
        }
        spannableStringBuilder.append(text);

        final int length = hasSpace ? SEPARATOR.length() + text.length() : text.length();

        ViewUtils.setColorInSpannable(textColor, indexStart, indexStart + length, spannableStringBuilder);
        ViewUtils.setFontInSpannable(context, font, spannableStringBuilder, indexStart, indexStart + length);

        return spannableStringBuilder;
    }
}
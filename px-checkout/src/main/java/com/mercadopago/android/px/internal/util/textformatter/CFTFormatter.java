package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadolibre.android.ui.font.Font;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.PayerCost;

public class CFTFormatter extends ChainFormatter {

    private PayerCost payerCost;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private String fontStylePath;

    public CFTFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final PayerCost payerCost) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        this.payerCost = payerCost;
        fontStylePath = Font.REGULAR.getFontPath();
    }

    public CFTFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public Spannable build() {
        return apply(payerCost.getCFTPercent());
    }

    @Override
    protected Spannable apply(@NonNull final CharSequence amount) {
        if (TextUtil.isEmpty(amount)) {
            return spannableStringBuilder;
        }
        final int initialIndex = spannableStringBuilder.length();
        final String cftDescription = context.getString(R.string.px_installments_cft, amount);
        final String separator = " ";
        spannableStringBuilder.append(separator).append(cftDescription);
        final int textLength = separator.length() + cftDescription.length();

        updateTextColor(initialIndex, initialIndex + textLength);
        updateTextFont(initialIndex, initialIndex + textLength);
        return spannableStringBuilder;
    }

    private void updateTextColor(final int indexStart, final int indexEnd) {
        if (textColor != 0) {
            ViewUtils.setColorInSpannable(textColor, indexStart, indexEnd, spannableStringBuilder);
        }
    }

    private void updateTextFont(final int indexStart, final int indexEnd) {
        if (fontStylePath != null) {
            ViewUtils
                .setFontInSpannable(indexStart, indexEnd, spannableStringBuilder, fontStylePath, context);
        }
    }
}

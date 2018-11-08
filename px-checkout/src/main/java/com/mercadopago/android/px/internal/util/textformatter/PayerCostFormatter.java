package com.mercadopago.android.px.internal.util.textformatter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadolibre.android.ui.font.Font;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.PayerCost;

public class PayerCostFormatter {

    private PayerCost payerCost;
    private String currencyId;
    private int textColor;
    private final Context context;
    private final SpannableStringBuilder spannableStringBuilder;
    private String fontStylePath;

    public PayerCostFormatter(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final PayerCost payerCost, @NonNull final String currencyId) {
        this.spannableStringBuilder = spannableStringBuilder;
        this.context = context;
        this.payerCost = payerCost;
        this.currencyId = currencyId;
        fontStylePath = Font.REGULAR.getFontPath();
    }

    public PayerCostFormatter withTextColor(final int color) {
        textColor = color;
        return this;
    }

    public Spannable apply() {

        final Spannable totalAmount = TextFormatter.withCurrencyId(currencyId)
            .amount(payerCost.getTotalAmount())
            .normalDecimals()
            .apply(R.string.px_total_amount_holder, context);

        final int initialIndex = spannableStringBuilder.length();
        final String separator = " ";
        spannableStringBuilder.append(separator).append(totalAmount);
        final int textLength = separator.length() + totalAmount.length();

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

package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import com.mercadopago.android.px.model.Discount;

public class DiscountDescriptionLocalized implements ILocalizedCharSequence {

    private final Discount discount;

    public DiscountDescriptionLocalized(@NonNull final Discount discount) {
        this.discount = discount;
    }

    @Override
    public CharSequence get(@NonNull final Context context) {
        final Editable spannableStringBuilder = new SpannableStringBuilder();

        if (discount.hasPercentOff()) {
            final Spannable spannable =
                TextFormatter.withCurrencyId(discount.getCurrencyId())
                    .noSpace()
                    .noSymbol()
                    .amount(discount.getPercentOff())
                    .normalDecimals()
                    .toSpannable();
            spannableStringBuilder
                .append(context.getResources().getString(R.string.px_discount_percent_off, spannable));
        } else {
            final Spannable spannable =
                TextFormatter.withCurrencyId(discount.getCurrencyId())
                    .withSpace()
                    .amount(discount.getAmountOff())
                    .normalDecimals()
                    .toSpannable();
            spannableStringBuilder.append(context.getResources().getString(R.string.px_discount_amount_off, spannable));
        }

        return spannableStringBuilder;
    }
}

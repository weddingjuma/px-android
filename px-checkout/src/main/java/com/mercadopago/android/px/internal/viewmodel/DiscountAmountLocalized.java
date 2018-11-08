package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import java.math.BigDecimal;

public class DiscountAmountLocalized implements ILocalizedCharSequence {

    private final BigDecimal amount;
    private final String currencyId;

    public DiscountAmountLocalized(@NonNull final BigDecimal amount, @NonNull final String currencyId) {
        this.amount = amount;
        this.currencyId = currencyId;
    }

    @Override
    public CharSequence get(@NonNull final Context context) {
        final Editable spannableStringBuilder = new SpannableStringBuilder();
        final Spannable spannable =
            TextFormatter.withCurrencyId(currencyId)
                .withSpace()
                .amount(amount)
                .normalDecimals()
                .toSpannable();
        spannableStringBuilder.append(context.getResources().getString(R.string.px_prefix_discount_amount, spannable));
        return spannableStringBuilder;
    }
}

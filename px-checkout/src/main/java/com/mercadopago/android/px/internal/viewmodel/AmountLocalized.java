package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import java.math.BigDecimal;

public class AmountLocalized implements ILocalizedCharSequence {

    @NonNull private final BigDecimal amount;
    @NonNull private final String currencyId;

    public AmountLocalized(@NonNull final BigDecimal amount, @NonNull final String currencyId) {
        this.amount = amount;
        this.currencyId = currencyId;
    }

    @Override
    public CharSequence get(@NonNull final Context context) {
        return TextFormatter.withCurrencyId(currencyId)
            .withSpace().amount(amount)
            .normalDecimals()
            .toSpannable();
    }
}

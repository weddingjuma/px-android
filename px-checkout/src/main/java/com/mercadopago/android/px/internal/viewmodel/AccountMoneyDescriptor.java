package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.textformatter.InstallmentFormatter;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.model.AccountMoneyMetadata;

public class AccountMoneyDescriptor extends InstallmentsDescriptorView.Model {

    private final AccountMoneyMetadata accountMoneyMetadata;

    @NonNull
    public static InstallmentsDescriptorView.Model createFrom(
        @NonNull final AccountMoneyMetadata accountMoneyMetadata) {
        return new AccountMoneyDescriptor(accountMoneyMetadata);
    }

    /* default */ AccountMoneyDescriptor(@NonNull final AccountMoneyMetadata accountMoneyMetadata) {
        this.accountMoneyMetadata = accountMoneyMetadata;
    }

    @Override
    public boolean isEmpty() {
        return !accountMoneyMetadata.invested;
    }

    @Override
    public void updateInstallmentsDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final TextView textView) {
        final InstallmentFormatter installmentFormatter = new InstallmentFormatter(spannableStringBuilder, context)
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey));
        installmentFormatter.build(context.getString(R.string.px_invested_account_money));
    }
}

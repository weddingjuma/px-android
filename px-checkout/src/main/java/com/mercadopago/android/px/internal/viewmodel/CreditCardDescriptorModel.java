package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.textformatter.AmountLabeledFormatter;
import com.mercadopago.android.px.internal.util.textformatter.CFTFormatter;
import com.mercadopago.android.px.internal.util.textformatter.PayerCostFormatter;
import com.mercadopago.android.px.internal.util.textformatter.SpannableFormatter;
import com.mercadopago.android.px.internal.util.textformatter.TextFormatter;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.Currency;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.internal.Text;
import java.math.BigDecimal;

/**
 * Model used to instantiate PaymentMethodDescriptorView for payment methods with payer costs. This model is used for
 * credit_card
 */
public final class CreditCardDescriptorModel extends PaymentMethodDescriptorView.Model {

    private final Currency currency;
    private final AmountConfiguration amountConfiguration;
    @StringRes private int installmentsText;
    @ColorRes private int installmentsColor;
    private int installments;

    @NonNull
    public static PaymentMethodDescriptorView.Model createFrom(@NonNull final Currency currency,
        @NonNull final AmountConfiguration amountConfiguration) {
        return new CreditCardDescriptorModel(currency, amountConfiguration);
    }

    @Override
    public boolean hasPayerCostList() {
        return amountConfiguration.getAppliedPayerCost(userWantToSplit).size() > 1;
    }

    private CreditCardDescriptorModel(@NonNull final Currency currency,
        @NonNull final AmountConfiguration amountConfiguration) {
        this.currency = currency;
        this.amountConfiguration = amountConfiguration;
        configureInstallmentsInfo();
    }

    private void configureInstallmentsInfo() {
        int maxInstallment = 0;
        int maxInterestsFreeInstallment = 0;
        for (final PayerCost payerCost : amountConfiguration.getPayerCosts()) {
            final int installments = payerCost.getInstallments();
            final Text interest = payerCost.getInterest();
            maxInstallment = Math.max(maxInstallment, installments);
            if (interest != null && TextUtil.isNotEmpty(interest.getMessage())) {
                maxInterestsFreeInstallment = Math.max(maxInterestsFreeInstallment, installments);
            }
        }

        switch (maxInterestsFreeInstallment) {
        case 0:
        case 1:
            installments = maxInstallment;
            installmentsText = maxInstallment > 1 ? R.string.px_installments_info : 0;
            installmentsColor = R.color.px_expressCheckoutInstallmentTitle;
            break;
        default:
            installments = maxInterestsFreeInstallment;
            installmentsText = R.string.px_installments_interest_free_info;
            installmentsColor = R.color.px_color_highlight;
            break;
        }
    }

    @Override
    public void updateLeftSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final TextView textView) {
        final Context context = textView.getContext();
        updateInstallment(spannableStringBuilder, context, textView);
        updateTotalAmountDescriptionSpannable(spannableStringBuilder, context);
        updateInterestDescriptionSpannable(spannableStringBuilder, context);
        updateCFTSpannable(spannableStringBuilder, context);
    }

    @Override
    public void updateRightSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final TextView textView) {
        super.updateRightSpannable(spannableStringBuilder, textView);
        updateInstallmentsInfo(spannableStringBuilder, textView.getContext());
    }

    private void updateInstallmentsInfo(@NonNull final SpannableStringBuilder spannableStringBuilder,
        final Context context) {
        if (payerCostSelected != PayerCost.NO_SELECTED) {
            return;
        }
        final String text = TextUtil.format(context.getString(installmentsText), String.valueOf(installments));
        new SpannableFormatter(spannableStringBuilder, context)
            .withStyle(PxFont.SEMI_BOLD)
            .withTextColor(ContextCompat.getColor(context, installmentsColor))
            .apply(text);
    }

    private void updateInstallment(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context,
        @NonNull final TextView textView) {

        final Spannable amount = TextFormatter.withCurrency(currency)
            .amount(getCurrent().getInstallmentAmount())
            .normalDecimals()
            .into(textView)
            .toSpannable();

        new AmountLabeledFormatter(spannableStringBuilder, context)
            .withInstallment(getCurrent().getInstallments())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_black))
            .withSemiBoldStyle()
            .apply(amount);
    }

    /**
     * Updates total amount the user will pay with credit card, only if there are interests involved.
     */
    private void updateTotalAmountDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        if (BigDecimal.ZERO.compareTo(getCurrent().getInstallmentRate()) < 0) {
            new PayerCostFormatter(spannableStringBuilder, context, getCurrent(), currency)
                .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey))
                .apply();
        }
    }

    private void updateInterestDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        final Text interest = getCurrent().getInterest();
        if (interest != null && TextUtil.isNotEmpty(interest.getMessage())) {
            new SpannableFormatter(spannableStringBuilder, context)
                .withTextColor(Color.parseColor(interest.getTextColor()))
                .withSpace()
                .apply(interest.getMessage());
        }
    }

    private void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        new CFTFormatter(spannableStringBuilder, context, getCurrent())
            .withTextColor(ContextCompat.getColor(context, R.color.ui_meli_grey)).build();
    }

    @NonNull
    private PayerCost getCurrent() {
        return amountConfiguration.getCurrentPayerCost(userWantToSplit, payerCostSelected);
    }
}

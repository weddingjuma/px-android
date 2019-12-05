package com.mercadopago.android.px.internal.features.express.installments;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.font.FontHelper;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.CurrenciesUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.Currency;
import com.mercadopago.android.px.model.PayerCost;
import java.util.Locale;

//TODO unify with normal installments.

/* default */ class InstallmentRowHolder extends RecyclerView.ViewHolder {

    private final View container;
    private final TextView installmentsText;
    private final MPTextView reimbursement;
    private final MPTextView installmentsInterestTop;
    private final MPTextView installmentsInterestCenter;

    /* default */ InstallmentRowHolder(final View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.container);
        installmentsText = itemView.findViewById(R.id.mpsdkInstallmentsText);
        installmentsInterestTop = itemView.findViewById(R.id.mpsdkInstallmentsInterestTop);
        reimbursement = itemView.findViewById(R.id.mpsdkReimbursement);
        installmentsInterestCenter = itemView.findViewById(R.id.mpsdkInstallmentsInterest);
    }

    /* default */ void populate(final InstallmentsAdapter.ItemListener itemListener, @NonNull final Currency currency,
        @NonNull final PayerCost payerCost, final boolean hasBenefits) {
        final int visibility = hasBenefits ? View.INVISIBLE : View.GONE;
        setInstallmentsText(currency, payerCost);
        final boolean hasReimbursement = loadReimbursement(payerCost, visibility);
        loadInstallmentsInterest(payerCost, currency, hasReimbursement, visibility);
        hideUnusedViews(hasReimbursement, visibility);
        itemView.setOnClickListener(v -> itemListener.onClick(payerCost));
    }

    private boolean loadReimbursement(@NonNull final PayerCost payerCost, final int visibility) {
        return ViewUtils.loadOrHide(visibility, payerCost.getReimbursement(), reimbursement);
    }

    private void loadInstallmentsInterest(@NonNull final PayerCost payerCost, @NonNull final Currency currency,
        final boolean hasReimbursement, final int visibility) {
        final MPTextView installmentsInterest = hasReimbursement ? installmentsInterestTop : installmentsInterestCenter;
        final boolean interestFree = ViewUtils.loadOrHide(visibility, payerCost.getInterest(), installmentsInterest);
        if (!interestFree) {
            ViewUtils.loadOrGone(getAmountWithRateText(currency, payerCost), installmentsInterest);
            installmentsInterest
                .setTextColor(ContextCompat.getColor(installmentsInterest.getContext(), R.color.px_color_payer_costs));
            FontHelper.setFont(installmentsInterest, PxFont.REGULAR);
        }
    }

    private void hideUnusedViews(final boolean hasReimbursement, final int visibility) {
        final MPTextView viewToHide = hasReimbursement ? installmentsInterestCenter : installmentsInterestTop;
        viewToHide.setVisibility(visibility);
    }

    private CharSequence getAmountWithRateText(@NonNull final Currency currency, @NonNull final PayerCost payerCost) {
        final Spanned spannedInstallmentsText =
            CurrenciesUtil.getSpannedAmountWithCurrencySymbol(payerCost.getTotalAmount(), currency);
        return TextUtils.concat("(", spannedInstallmentsText, ")");
    }

    private void setInstallmentsText(@NonNull final Currency currency, @NonNull final PayerCost payerCost) {

        final Spanned spannedInstallmentsText =
            CurrenciesUtil.getSpannedAmountWithCurrencySymbol(payerCost.getInstallmentAmount(), currency);

        final String text = installmentsText.getContext().getString(R.string.px_installments_by);

        installmentsText.setText(
            new SpannableStringBuilder(String.format(Locale.getDefault(), "%d", payerCost.getInstallments()))
                .append(text).append(" ")
                .append(spannedInstallmentsText));
    }

    /* default */ void highLight() {
        container.setSelected(true);
    }

    /* default */ void noHighLight() {
        container.setSelected(false);
    }
}
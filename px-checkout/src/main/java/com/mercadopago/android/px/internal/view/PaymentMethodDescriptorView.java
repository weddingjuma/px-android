package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.PayerCost;

public class PaymentMethodDescriptorView extends ConstraintLayout {

    final MPTextView leftText;
    final MPTextView rightText;

    public PaymentMethodDescriptorView(final Context context) {
        this(context, null);
    }

    public PaymentMethodDescriptorView(final Context context,
        @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentMethodDescriptorView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.px_view_payment_method_descriptor, this);
        leftText = findViewById(R.id.left_text);
        rightText = findViewById(R.id.right_text);
    }

    public void update(@NonNull final Model model) {
        final SpannableStringBuilder leftSpannableBuilder = new SpannableStringBuilder();
        model.updateLeftSpannable(leftSpannableBuilder, leftText);
        leftText.setText(leftSpannableBuilder);
        final SpannableStringBuilder rightSpannableBuilder = new SpannableStringBuilder();
        model.updateRightSpannable(rightSpannableBuilder, leftText);
        rightText.setText(rightSpannableBuilder);
    }

    public abstract static class Model {
        protected int payerCostSelected = PayerCost.NO_SELECTED;
        protected boolean userWantToSplit = true;

        public abstract void updateLeftSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final TextView textView);

        public void updateRightSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
            @NonNull final TextView textView) {
        }

        public final void setCurrentPayerCost(final int payerCostSelected) {
            this.payerCostSelected = payerCostSelected;
        }

        public int getCurrentInstalment() {
          return PayerCost.NO_SELECTED;
        }

        public final void setSplit(final boolean split) {
            userWantToSplit = split;
        }

        public boolean hasPayerCostList() {
            return false;
        }
    }
}
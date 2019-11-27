package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.textformatter.SpannableFormatter;
import com.mercadopago.android.px.internal.view.PaymentMethodDescriptorView;
import com.mercadopago.android.px.model.internal.Text;

public class DisabledPaymentMethodDescriptorModel extends PaymentMethodDescriptorView.Model {

    @Nullable public Text message;

    @NonNull
    public static PaymentMethodDescriptorView.Model createFrom(@NonNull final Text message) {
        return new DisabledPaymentMethodDescriptorModel(message);
    }

    /* default */ DisabledPaymentMethodDescriptorModel(@Nullable final Text message) {
        this.message = message;
    }

    @Override
    public void updateSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final TextView textView) {
        final Context context = textView.getContext();
        final SpannableFormatter amountLabeledFormatter = new SpannableFormatter(spannableStringBuilder, context);
        amountLabeledFormatter.withTextColor(ContextCompat.getColor(context, R.color.ui_meli_blue))
            .withStyle(PxFont.SEMI_BOLD);
        if (message != null && TextUtil.isNotEmpty(message.getMessage())) {
            amountLabeledFormatter.apply(message.getMessage());
        } else {
            amountLabeledFormatter.apply(R.string.px_payment_method_disable_title);
        }
    }
}
package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;

public class EmptyInstallmentsDescriptor extends InstallmentsDescriptorView.Model {

    protected EmptyInstallmentsDescriptor() {
        super();
    }

    @Override
    public void updateInstallmentsDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context, @NonNull final CharSequence amount, @NonNull final TextView textView) {
        //Do nothing
    }

    @Override
    public void updateInterestDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        //Do nothing
    }

    @Override
    public void updateTotalAmountDescriptionSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        //Do nothing
    }

    @Override
    public void updateCFTSpannable(@NonNull final SpannableStringBuilder spannableStringBuilder,
        @NonNull final Context context) {
        //Do nothing
    }

    public static InstallmentsDescriptorView.Model create() {
        return new EmptyInstallmentsDescriptor();
    }
}

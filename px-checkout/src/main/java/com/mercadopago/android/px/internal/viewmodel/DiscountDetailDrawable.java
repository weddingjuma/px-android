package com.mercadopago.android.px.internal.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.mercadopago.android.px.R;

public class DiscountDetailDrawable implements IDetailDrawable {

    @Override
    public Drawable getDrawable(@NonNull final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.px_helper);
    }
}

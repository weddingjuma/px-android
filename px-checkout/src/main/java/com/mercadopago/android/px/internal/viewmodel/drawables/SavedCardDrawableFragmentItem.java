package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.CardDisplayInfo;

public class SavedCardDrawableFragmentItem implements DrawableFragmentItem {

    @NonNull public final String paymentMethodId;
    @NonNull public final CardDisplayInfo card;

    public SavedCardDrawableFragmentItem(@NonNull final String paymentMethodId, @NonNull final CardDisplayInfo card) {
        this.paymentMethodId = paymentMethodId;
        this.card = card;
    }

    @Override
    public Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer) {
        return drawer.draw(this);
    }
}

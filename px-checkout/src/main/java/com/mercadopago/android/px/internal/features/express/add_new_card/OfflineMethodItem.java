package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.internal.Text;

/* default */ final class OfflineMethodItem {

    @NonNull private final Text name;
    @Nullable private Text description;
    private int iconId;

    public OfflineMethodItem(@NonNull final Text name) {
        this.name = name;
    }

    public OfflineMethodItem(@NonNull final Text name, @NonNull final Text description, final int iconId) {
        this.name = name;
        this.description = description;
        this.iconId = iconId;
    }

    @NonNull
    public Text getName() {
        return name;
    }

    @Nullable
    public Text getDescription() {
        return description;
    }

    public int getIconId() {
        return iconId;
    }

    public boolean isOfflinePaymentTypeItem() {
        return description == null && iconId == 0;
    }
}

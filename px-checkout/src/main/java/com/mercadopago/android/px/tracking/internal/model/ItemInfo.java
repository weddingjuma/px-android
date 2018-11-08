package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;

public class ItemInfo implements Serializable {

    private int quantity;
    private String currencyId;
    private ItemDetail item;

    public ItemInfo(@Nullable final String id, @Nullable final String description, @NonNull final BigDecimal price,
        final int quantity, @NonNull final String currencyId) {
        this.quantity = quantity;
        this.currencyId = currencyId;
        item = new ItemDetail(id, description, price);
    }
}

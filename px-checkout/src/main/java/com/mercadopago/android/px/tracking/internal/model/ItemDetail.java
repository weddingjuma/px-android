package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.math.BigDecimal;

public class ItemDetail {

    private String id;
    private String description;
    private BigDecimal price;

    public ItemDetail(@Nullable final String id, @Nullable final String description, @NonNull final BigDecimal price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }
}

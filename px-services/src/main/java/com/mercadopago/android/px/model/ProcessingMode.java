package com.mercadopago.android.px.model;

import com.google.gson.annotations.SerializedName;

public enum ProcessingMode {
    @SerializedName("aggregator") AGGREGATOR,
    @SerializedName("gateway") GATEWAY;

    public String asQueryParamName() {
        return name().toLowerCase();
    }
}

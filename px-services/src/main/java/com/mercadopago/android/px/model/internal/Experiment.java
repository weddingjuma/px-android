package com.mercadopago.android.px.model.internal;

import java.io.Serializable;

public class Experiment implements Serializable {

    private String id;
    private String name;
    private Variant variant;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Variant getVariant() {
        return variant;
    }
}

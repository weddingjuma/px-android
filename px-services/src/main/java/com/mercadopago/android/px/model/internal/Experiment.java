package com.mercadopago.android.px.model.internal;

import java.io.Serializable;

public class Experiment implements Serializable {

    private int id;
    private String name;
    private Variant variant;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Variant getVariant() {
        return variant;
    }
}

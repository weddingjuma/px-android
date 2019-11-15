package com.mercadopago.android.px.model.internal;

import java.io.Serializable;
import java.util.List;

public class Variant implements Serializable {

    private int id;
    private String name;
    private List<AvailableFeature> availableFeatures;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<AvailableFeature> getAvailableFeatures() {
        return availableFeatures;
    }
}

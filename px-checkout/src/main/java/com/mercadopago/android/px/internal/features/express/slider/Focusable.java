package com.mercadopago.android.px.internal.features.express.slider;

public interface Focusable {

    void onFocusIn();

    void onFocusOut();

    boolean hasFocus();
}
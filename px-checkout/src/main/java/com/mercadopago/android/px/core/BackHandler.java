package com.mercadopago.android.px.core;

public interface BackHandler {

    /**
     *
     * @return return false if caller should not handle back
     */
    boolean handleBack();
}
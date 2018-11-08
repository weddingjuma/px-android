package com.mercadopago.android.px.internal.core;

public final class Settings {

    public static String servicesVersion = "v1";

    private Settings() {
        //Do nothing
    }

    @Deprecated
    public static void enableBetaServices() {
        servicesVersion = "beta";
    }
}
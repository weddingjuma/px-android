package com.mercadopago.android.px.model;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({ CheckoutTypes.ONE_TAP, CheckoutTypes.TRADITIONAL })

public @interface CheckoutTypes {
    String ONE_TAP = "one_tap";
    String TRADITIONAL = "traditional";
}


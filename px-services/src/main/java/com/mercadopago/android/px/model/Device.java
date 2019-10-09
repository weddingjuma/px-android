package com.mercadopago.android.px.model;

import android.content.Context;
import java.io.Serializable;

public class Device implements Serializable {

    public Fingerprint fingerprint;

    public Device(final Context context) {
        fingerprint = new Fingerprint(context);
    }
}

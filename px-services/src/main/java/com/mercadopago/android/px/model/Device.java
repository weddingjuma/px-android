package com.mercadopago.android.px.model;

import android.content.Context;
import java.io.Serializable;

public class Device {

    public Fingerprint fingerprint;

    public Device(final Context context) {
        fingerprint = new Fingerprint(context);
    }
}

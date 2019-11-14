package com.mercadopago.android.px.internal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import com.squareup.picasso.Picasso;

public final class PicassoLoader {

    @SuppressLint("StaticFieldLeak")
    private static Picasso picasso;

    private PicassoLoader() {
    }

    public static void initialize(@NonNull final Context context) {
        if (picasso == null) {
            picasso =
                new Picasso.Builder(context.getApplicationContext() == null ? context : context.getApplicationContext())
                    .build();
        }
    }

    public static Picasso getPicasso() {
        if (picasso == null) {
            throw new ExceptionInInitializerError("Picasso is not initialized");
        }
        return picasso;
    }
}
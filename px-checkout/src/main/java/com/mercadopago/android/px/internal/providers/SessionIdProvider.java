package com.mercadopago.android.px.internal.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.UUID;

public final class SessionIdProvider {

    private static String id;
    private static final String PREF_SESSION_ID = "PREF_SESSION_ID";

    private final Context context;

    public SessionIdProvider(@NonNull final Context context) {
        this.context = context;
    }

    public void create() {
        id = UUID.randomUUID().toString();
        getSharedPreference().edit().putString(PREF_SESSION_ID, id).apply();
    }

    @Nullable
    public String getSessionId() {
        if (id == null) {
            return getSharedPreference().getString(PREF_SESSION_ID, null);
        } else {
            return id;
        }
    }

    private SharedPreferences getSharedPreference() {
        return context.getSharedPreferences("com.mercadopago.checkout.store", Context.MODE_PRIVATE);
    }
}

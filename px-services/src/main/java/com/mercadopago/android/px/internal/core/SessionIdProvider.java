package com.mercadopago.android.px.internal.core;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.UUID;

public final class SessionIdProvider {

    private static final String PREF_SESSION_ID = "PREF_SESSION_ID";
    @NonNull private final SharedPreferences sharedPreferences;

    @Nullable private String id;

    public static SessionIdProvider create(@NonNull final SharedPreferences sharedPreferences) {
        final SessionIdProvider instance = new SessionIdProvider(sharedPreferences);
        instance.id = UUID.randomUUID().toString();
        sharedPreferences.edit().putString(PREF_SESSION_ID, instance.id).apply();
        return instance;
    }

    public static SessionIdProvider createFromStorage(@NonNull final SharedPreferences sharedPreferences) {
        final SessionIdProvider instance = new SessionIdProvider(sharedPreferences);
        instance.id = sharedPreferences.getString(PREF_SESSION_ID, "no-value");
        return instance;
    }

    private SessionIdProvider(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    public String getSessionId() {
        if (id == null) {
            id = sharedPreferences.getString(PREF_SESSION_ID, "no-value");
        }
        return id;
    }
}

package com.mercadopago.android.px.internal.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.internal.util.RetrofitUtil;
import java.io.File;
import retrofit2.Retrofit;

public class ApplicationModule implements PreferenceComponent {

    @NonNull
    private final Context context;
    private SessionIdProvider sessionIdProvider;

    public ApplicationModule(@NonNull final Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    @NonNull
    public SessionIdProvider getSessionIdProvider() {
        if (sessionIdProvider == null) {
            sessionIdProvider = SessionIdProvider.createFromStorage(getSharedPreferences());
        }
        return sessionIdProvider;
    }

    @NonNull
    public SessionIdProvider newSessionIdProvider() {
        sessionIdProvider = SessionIdProvider.create(getSharedPreferences());
        return sessionIdProvider;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("com.mercadopago.checkout.store", Context.MODE_PRIVATE);
    }

    public JsonUtil getJsonUtil() {
        return JsonUtil.getInstance();
    }

    public FileManager getFileManager() {
        return new FileManager();
    }

    public File getCacheDir() {
        return context.getCacheDir();
    }

    public Retrofit getRetrofitClient() {
        return RetrofitUtil.getRetrofitClient(context);
    }
}

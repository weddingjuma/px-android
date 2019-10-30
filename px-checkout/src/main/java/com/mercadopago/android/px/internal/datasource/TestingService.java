package com.mercadopago.android.px.internal.datasource;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.repository.TestingRepository;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.Experiment;
import java.util.List;

public class TestingService implements TestingRepository {

    private static final String PREF_EXPERIMENTS = "PREF_EXPERIMENTS";

    @NonNull private final SharedPreferences sharedPreferences;

    @Nullable private List<Experiment> experiments;

    public TestingService(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void reset() {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().apply();
        experiments = null;
    }

    @Override
    public void configure(@Nullable final List<Experiment> experiments) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(PREF_EXPERIMENTS, JsonUtil.toJson(experiments));
        edit.apply();
    }

    @Nullable
    public List<Experiment> getExperiments() {
        return experiments;
    }
}

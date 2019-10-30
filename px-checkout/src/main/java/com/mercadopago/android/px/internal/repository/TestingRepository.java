package com.mercadopago.android.px.internal.repository;

import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.Experiment;
import java.util.List;

public interface TestingRepository {

    void reset();

    void configure(@Nullable final List<Experiment> experiments);
}

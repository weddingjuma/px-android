package com.mercadopago.android.px.internal.repository;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.callbacks.MPCall;
import com.mercadopago.android.px.model.internal.InitResponse;

public interface InitRepository {

    @NonNull
    MPCall<InitResponse> init();

    MPCall<InitResponse> cleanRefresh();

    MPCall<InitResponse> refresh();

    MPCall<InitResponse> refreshWithNewCard(@NonNull final String cardId);

    void addOnChangedListener(@NonNull final OnChangedListener listener);

    interface OnChangedListener {
        void onInitResponseChanged(@NonNull final InitResponse response);
    }
}
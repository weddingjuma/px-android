package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public abstract class NonNullMapper<T, V> extends Mapper<T, V> {
    @Override
    public List<V> map(@NonNull final Iterable<T> val) {
        final List<V> returned = new ArrayList<>();
        for (final T value : val) {
            final V mappedValue = map(value);
            if (mappedValue != null) {
                returned.add(mappedValue);
            }
        }
        return returned;
    }
}
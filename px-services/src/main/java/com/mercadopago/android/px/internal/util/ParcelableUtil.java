package com.mercadopago.android.px.internal.util;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public final class ParcelableUtil {

    private ParcelableUtil() {
    }

    @Nullable
    public static BigDecimal getOptionalBigDecimal(final Parcel in) {
        if (in.readByte() == 0) {
            return null;
        } else {
            return new BigDecimal(in.readString());
        }
    }

    @NonNull
    public static BigDecimal getBigDecimal(final Parcel in) {
        return new BigDecimal(in.readString());
    }

    @Nullable
    public static Integer getOptionalInteger(final Parcel in) {
        if (in.readByte() == 0) {
            return null;
        } else {
            return in.readInt();
        }
    }

    public static void write(final Parcel dest, final BigDecimal number) {
        dest.writeString(number.toString());
    }

    public static void writeOptional(final Parcel dest, @Nullable final BigDecimal number) {
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(number.toString());
        }
    }

    public static void writeOptional(final Parcel dest, @Nullable final Integer number) {
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(number);
        }
    }

    // For writing to a Serializable
    public static <K extends Serializable, V extends Serializable> void writeSerializableMap(
        Parcel parcel, Map<K, V> map) {
        parcel.writeInt(map.size());
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (e.getValue() != null) {
                parcel.writeSerializable(e.getKey());
                parcel.writeSerializable(e.getValue());
            }
        }
    }

    // For reading from a Serializable
    public static <K extends Serializable, V extends Serializable> void readSerializableMap(@NonNull final Map<K,V> map,
        final Parcel parcel, @NonNull final Class<K> kClass, @NonNull final Class<V> vClass) {
        int size = parcel.readInt();
        for (int i = 0; i < size; i++) {
            map.put(Objects.requireNonNull(kClass.cast(parcel.readSerializable())),
                Objects.requireNonNull(vClass.cast(parcel.readSerializable())));
        }
    }
}
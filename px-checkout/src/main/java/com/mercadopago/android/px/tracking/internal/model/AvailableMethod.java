package com.mercadopago.android.px.tracking.internal.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.PaymentMethod;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used for Payment vault and Express checkout screen.
 */
@SuppressWarnings("unused")
@Keep
public class AvailableMethod extends TrackingMapModel implements Parcelable {

    @Nullable
    /* default */ final String paymentMethodId;
    @NonNull
    /* default */ final String paymentMethodType;
    @Nullable
    /* default */ final Map<String, Object> extraInfo;

    public static final Creator<AvailableMethod> CREATOR = new Creator<AvailableMethod>() {
        @Override
        public AvailableMethod createFromParcel(final Parcel in) {
            return new AvailableMethod(in);
        }

        @Override
        public AvailableMethod[] newArray(final int size) {
            return new AvailableMethod[size];
        }
    };

    public static AvailableMethod from(@NonNull final PaymentMethod paymentMethod) {
        return new AvailableMethod(paymentMethod.getId(), paymentMethod.getPaymentTypeId());
    }

    public AvailableMethod(@NonNull final String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
        paymentMethodId = null;
        extraInfo = null;
    }

    public AvailableMethod(@NonNull final String paymentMethodId,
        @NonNull final String paymentMethodType,
        @NonNull final Map<String, Object> extraInfo) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodType = paymentMethodType;
        this.extraInfo = extraInfo;
    }

    public AvailableMethod(@Nullable final String paymentMethodId,
        @NonNull final String paymentMethodType) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodType = paymentMethodType;
        extraInfo = null;
    }

    protected AvailableMethod(final Parcel in) {
        paymentMethodId = in.readString();
        paymentMethodType = in.readString();
        extraInfo = new HashMap<>();
        in.readMap(extraInfo, Object.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        parcel.writeString(paymentMethodId);
        parcel.writeString(paymentMethodType);
        parcel.writeMap(extraInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
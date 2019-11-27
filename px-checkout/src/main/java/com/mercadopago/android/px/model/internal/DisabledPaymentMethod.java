package com.mercadopago.android.px.model.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class DisabledPaymentMethod implements Parcelable {

    @NonNull private String paymentMethodId;
    @Nullable private String paymentStatusDetail;

    public static final Creator<DisabledPaymentMethod> CREATOR = new Creator<DisabledPaymentMethod>() {
        @Override
        public DisabledPaymentMethod createFromParcel(final Parcel in) {
            return new DisabledPaymentMethod(in);
        }

        @Override
        public DisabledPaymentMethod[] newArray(final int size) {
            return new DisabledPaymentMethod[size];
        }
    };

    public DisabledPaymentMethod(@NonNull final String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public DisabledPaymentMethod(@NonNull final String paymentMethodId, @NonNull final String paymentStatusDetail) {
        this.paymentMethodId = paymentMethodId;
        this.paymentStatusDetail = paymentStatusDetail;
    }

    protected DisabledPaymentMethod(final Parcel in) {
        paymentMethodId = in.readString();
        paymentStatusDetail = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(paymentMethodId);
        dest.writeString(paymentStatusDetail);
    }

    @NonNull
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Nullable
    public String getPaymentStatusDetail() {
        return paymentStatusDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
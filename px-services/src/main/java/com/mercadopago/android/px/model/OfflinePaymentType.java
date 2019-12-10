package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.List;

public final class OfflinePaymentType implements Parcelable, Serializable {

    private final String id;
    private final String description;
    private final List<OfflinePaymentMethod> offlinePaymentMethods;

    public static final Creator<OfflinePaymentType> CREATOR = new Creator<OfflinePaymentType>() {
        @Override
        public OfflinePaymentType createFromParcel(final Parcel in) {
            return new OfflinePaymentType(in);
        }

        @Override
        public OfflinePaymentType[] newArray(final int size) {
            return new OfflinePaymentType[size];
        }
    };

    protected OfflinePaymentType(final Parcel in) {
        id = in.readString();
        description = in.readString();
        offlinePaymentMethods = in.createTypedArrayList(OfflinePaymentMethod.CREATOR);
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeTypedList(offlinePaymentMethods);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

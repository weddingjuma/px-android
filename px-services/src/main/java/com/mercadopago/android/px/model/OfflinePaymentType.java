package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;
import java.io.Serializable;
import java.util.List;

public final class OfflinePaymentType implements Parcelable, Serializable {

    private final String id;
    private final Text name;
    private final List<OfflinePaymentMethod> paymentMethods;

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
        name = in.readParcelable(Text.class.getClassLoader());
        paymentMethods = in.createTypedArrayList(OfflinePaymentMethod.CREATOR);
    }

    public Text getName() {
        return name;
    }

    public List<OfflinePaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeParcelable(name, flags);
        dest.writeTypedList(paymentMethods);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;
import java.io.Serializable;

public final class OfflinePaymentMethod implements Parcelable, Serializable {

    private final String id;
    private final Text name;
    private final Text description;
    private final boolean additionalInfoNeeded;

    public static final Creator<OfflinePaymentMethod> CREATOR = new Creator<OfflinePaymentMethod>() {
        @Override
        public OfflinePaymentMethod createFromParcel(final Parcel in) {
            return new OfflinePaymentMethod(in);
        }

        @Override
        public OfflinePaymentMethod[] newArray(final int size) {
            return new OfflinePaymentMethod[size];
        }
    };

    protected OfflinePaymentMethod(final Parcel in) {
        id = in.readString();
        name = in.readParcelable(Text.class.getClassLoader());
        description = in.readParcelable(Text.class.getClassLoader());
        additionalInfoNeeded = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeParcelable(name, flags);
        dest.writeParcelable(description, flags);
        dest.writeByte((byte) (additionalInfoNeeded ? 1 : 0));
    }
}

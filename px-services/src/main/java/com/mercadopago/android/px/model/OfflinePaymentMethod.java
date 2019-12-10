package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public final class OfflinePaymentMethod implements Parcelable, Serializable {

    private final String id;
    private final String description;
    private final String comment;
    private final boolean additionalInfoNeeded;
    private final int icon;

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
        description = in.readString();
        comment = in.readString();
        additionalInfoNeeded = in.readByte() != 0;
        icon = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(comment);
        dest.writeByte((byte) (additionalInfoNeeded ? 1 : 0));
        dest.writeInt(icon);
    }
}

package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class ConsumerCreditsMetadata implements Parcelable, Serializable {

    public final ConsumerCreditsDisplayInfo displayInfo;

    public static final Creator<ConsumerCreditsMetadata> CREATOR = new Creator<ConsumerCreditsMetadata>() {
        @Override
        public ConsumerCreditsMetadata createFromParcel(final Parcel in) {
            return new ConsumerCreditsMetadata(in);
        }

        @Override
        public ConsumerCreditsMetadata[] newArray(final int size) {
            return new ConsumerCreditsMetadata[size];
        }
    };

    /* default */ ConsumerCreditsMetadata(final Parcel in) {
        displayInfo = in.readParcelable(ConsumerCreditsDisplayInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(displayInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
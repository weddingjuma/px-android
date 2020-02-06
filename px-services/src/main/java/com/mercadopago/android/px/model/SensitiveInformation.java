package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class SensitiveInformation implements Parcelable {

    private final String firstName;
    private final String lastName;
    private final Identification identification;

    public static final Creator<SensitiveInformation> CREATOR = new Creator<SensitiveInformation>() {
        @Override
        public SensitiveInformation createFromParcel(final Parcel in) {
            return new SensitiveInformation(in);
        }

        @Override
        public SensitiveInformation[] newArray(final int size) {
            return new SensitiveInformation[size];
        }
    };

    protected SensitiveInformation(final Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        identification = in.readParcelable(Identification.class.getClassLoader());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Identification getIdentification() {
        return identification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeParcelable(identification, flags);
    }
}

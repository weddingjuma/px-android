package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class OfflineMethodsCompliance implements Parcelable {

    private final String turnComplianceDeepLink;
    private final boolean isCompliant;
    private final SensitiveInformation sensitiveInformation;

    public static final Creator<OfflineMethodsCompliance> CREATOR = new Creator<OfflineMethodsCompliance>() {
        @Override
        public OfflineMethodsCompliance createFromParcel(final Parcel in) {
            return new OfflineMethodsCompliance(in);
        }

        @Override
        public OfflineMethodsCompliance[] newArray(final int size) {
            return new OfflineMethodsCompliance[size];
        }
    };

    protected OfflineMethodsCompliance(final Parcel in) {
        turnComplianceDeepLink = in.readString();
        isCompliant = in.readByte() != 0;
        sensitiveInformation = in.readParcelable(SensitiveInformation.class.getClassLoader());
    }

    public String getTurnComplianceDeepLink() {
        return turnComplianceDeepLink;
    }

    public boolean isCompliant() {
        return isCompliant;
    }

    public SensitiveInformation getSensitiveInformation() {
        return sensitiveInformation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(turnComplianceDeepLink);
        dest.writeByte((byte) (isCompliant ? 1 : 0));
        dest.writeParcelable(sensitiveInformation, flags);
    }
}

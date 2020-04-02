package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

public final class OfflineMethodsCompliance extends InitiativeCompliance {

    private final String turnComplianceDeepLink;
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

    @SuppressWarnings("WeakerAccess")
    protected OfflineMethodsCompliance(final Parcel in) {
        super(in);
        turnComplianceDeepLink = in.readString();
        sensitiveInformation = in.readParcelable(SensitiveInformation.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NotNull final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(turnComplianceDeepLink);
        dest.writeParcelable(sensitiveInformation, flags);
    }

    public String getTurnComplianceDeepLink() {
        return turnComplianceDeepLink;
    }

    public SensitiveInformation getSensitiveInformation() {
        return sensitiveInformation;
    }

}
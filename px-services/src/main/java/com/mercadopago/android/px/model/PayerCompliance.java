package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class PayerCompliance implements Parcelable {

    private final OfflineMethodsCompliance offlineMethods;
    private final InitiativeCompliance ifpe;

    public static final Creator<PayerCompliance> CREATOR = new Creator<PayerCompliance>() {
        @Override
        public PayerCompliance createFromParcel(final Parcel in) {
            return new PayerCompliance(in);
        }

        @Override
        public PayerCompliance[] newArray(final int size) {
            return new PayerCompliance[size];
        }
    };

    protected PayerCompliance(final Parcel in) {
        offlineMethods = in.readParcelable(OfflineMethodsCompliance.class.getClassLoader());
        ifpe = in.readParcelable(InitiativeCompliance.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(offlineMethods, flags);
        dest.writeParcelable(ifpe, flags);
    }

    public OfflineMethodsCompliance getOfflineMethods() {
        return offlineMethods;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public InitiativeCompliance getIfpe() {
        return ifpe;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
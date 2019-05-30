package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public enum ProcessingMode implements Parcelable {
    @SerializedName("aggregator") AGGREGATOR,
    @SerializedName("gateway") GATEWAY;

    public String asQueryParamName() {
        return name().toLowerCase();
    }

    public static final Creator<ProcessingMode> CREATOR = new Creator<ProcessingMode>() {
        @Override
        public ProcessingMode createFromParcel(Parcel in) {
            return ProcessingMode.values()[in.readInt()];
        }

        @Override
        public ProcessingMode[] newArray(int size) {
            return new ProcessingMode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }
}
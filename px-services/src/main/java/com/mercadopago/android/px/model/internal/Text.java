package com.mercadopago.android.px.model.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public final class Text implements Parcelable, Serializable {

    public static final Text EMPTY = new Text();

    private final String message;
    private final String backgroundColor;
    private final String textColor;
    private final String weight;

    public static final Creator<Text> CREATOR = new Creator<Text>() {
        @Override
        public Text createFromParcel(final Parcel in) {
            return new Text(in);
        }

        @Override
        public Text[] newArray(final int size) {
            return new Text[size];
        }
    };

    private Text() {
        message = null;
        backgroundColor = null;
        textColor = null;
        weight = null;
    }

    /* default */ Text(final Parcel in) {
        message = in.readString();
        backgroundColor = in.readString();
        textColor = in.readString();
        weight = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(message);
        dest.writeString(backgroundColor);
        dest.writeString(textColor);
        dest.writeString(weight);
    }

    public String getMessage() {
        return message;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getWeight() {
        return weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
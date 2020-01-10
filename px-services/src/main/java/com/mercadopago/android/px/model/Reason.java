package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;

public final class Reason implements Parcelable {

    private final Text summary;
    private final Text title;
    private final Text description;

    public static final Creator<Reason> CREATOR = new Creator<Reason>() {
        @Override
        public Reason createFromParcel(final Parcel in) {
            return new Reason(in);
        }

        @Override
        public Reason[] newArray(final int size) {
            return new Reason[size];
        }
    };

    protected Reason(final Parcel in) {
        summary = in.readParcelable(Text.class.getClassLoader());
        title = in.readParcelable(Text.class.getClassLoader());
        description = in.readParcelable(Text.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(summary, flags);
        dest.writeParcelable(title, flags);
        dest.writeParcelable(description, flags);
    }

    public Text getSummary() {
        return summary;
    }

    public Text getTitle() {
        return title;
    }

    public Text getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
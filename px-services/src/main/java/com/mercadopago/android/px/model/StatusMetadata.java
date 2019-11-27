package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;
import java.io.Serializable;

public final class StatusMetadata implements Parcelable, Serializable {

    private final Text mainMessage;
    private final Text secondaryMessage;
    private final boolean enabled;

    public static final Creator<StatusMetadata> CREATOR = new Creator<StatusMetadata>() {
        @Override
        public StatusMetadata createFromParcel(final Parcel in) {
            return new StatusMetadata(in);
        }

        @Override
        public StatusMetadata[] newArray(final int size) {
            return new StatusMetadata[size];
        }
    };

    protected StatusMetadata(final Parcel in) {
        mainMessage = in.readParcelable(Text.class.getClassLoader());
        secondaryMessage = in.readParcelable(Text.class.getClassLoader());
        enabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mainMessage, flags);
        dest.writeParcelable(secondaryMessage, flags);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    public Text getMainMessage() {
        return mainMessage;
    }

    public Text getSecondaryMessage() {
        return secondaryMessage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
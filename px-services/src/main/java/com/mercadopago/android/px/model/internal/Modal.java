package com.mercadopago.android.px.model.internal;

import android.os.Parcel;
import android.os.Parcelable;

public final class Modal implements Parcelable {

    private final Text title;
    private final Text description;
    private final Action mainButton;
    private final Action secondaryButton;
    private final String iconUrl;

    public static final Creator<Modal> CREATOR = new Creator<Modal>() {
        @Override
        public Modal createFromParcel(final Parcel in) {
            return new Modal(in);
        }

        @Override
        public Modal[] newArray(final int size) {
            return new Modal[size];
        }
    };

    @SuppressWarnings({ "WeakerAccess", "ProtectedMemberInFinalClass" })
    protected Modal(final Parcel in) {
        title = in.readParcelable(Text.class.getClassLoader());
        description = in.readParcelable(Text.class.getClassLoader());
        mainButton = in.readParcelable(Action.class.getClassLoader());
        secondaryButton = in.readParcelable(Action.class.getClassLoader());
        iconUrl = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(title, flags);
        dest.writeParcelable(description, flags);
        dest.writeParcelable(mainButton, flags);
        dest.writeParcelable(secondaryButton, flags);
        dest.writeString(iconUrl);
    }

    public Text getTitle() {
        return title;
    }

    public Text getDescription() {
        return description;
    }

    public Action getMainButton() {
        return mainButton;
    }

    public Action getSecondaryButton() {
        return secondaryButton;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
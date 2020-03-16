package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;

public final class SliderDisplayInfo implements Parcelable {

    private final Text bottomDescription;

    public static final Creator<SliderDisplayInfo> CREATOR = new Creator<SliderDisplayInfo>() {
        @Override
        public SliderDisplayInfo createFromParcel(final Parcel in) {
            return new SliderDisplayInfo(in);
        }

        @Override
        public SliderDisplayInfo[] newArray(final int size) {
            return new SliderDisplayInfo[size];
        }
    };

    @SuppressWarnings({ "WeakerAccess", "ProtectedMemberInFinalClass" })
    protected SliderDisplayInfo(final Parcel in) {
        bottomDescription = in.readParcelable(Text.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(bottomDescription, flags);
    }

    public Text getBottomDescription() {
        return bottomDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
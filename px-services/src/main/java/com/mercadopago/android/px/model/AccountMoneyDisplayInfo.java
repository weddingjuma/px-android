package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class AccountMoneyDisplayInfo implements Serializable, Parcelable {

    public String message;
    public String sliderTitle;

    public static final Creator<AccountMoneyDisplayInfo> CREATOR = new Creator<AccountMoneyDisplayInfo>() {
        @Override
        public AccountMoneyDisplayInfo createFromParcel(Parcel in) {
            return new AccountMoneyDisplayInfo(in);
        }

        @Override
        public AccountMoneyDisplayInfo[] newArray(int size) {
            return new AccountMoneyDisplayInfo[size];
        }
    };

    protected AccountMoneyDisplayInfo(final Parcel in) {
        message = in.readString();
        sliderTitle = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(message);
        dest.writeString(sliderTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
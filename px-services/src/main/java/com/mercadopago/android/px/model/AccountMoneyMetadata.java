package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mercadopago.android.px.internal.util.ParcelableUtil;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountMoneyMetadata implements Serializable, Parcelable {

    public final String message;

    @SerializedName("available_balance")
    public final BigDecimal balance;

    public final boolean invested;

    protected AccountMoneyMetadata(final Parcel in) {
        message = in.readString();
        invested = in.readByte() != 0;
        balance = ParcelableUtil.getBigDecimal(in);
    }

    public static final Creator<AccountMoneyMetadata> CREATOR = new Creator<AccountMoneyMetadata>() {
        @Override
        public AccountMoneyMetadata createFromParcel(final Parcel in) {
            return new AccountMoneyMetadata(in);
        }

        @Override
        public AccountMoneyMetadata[] newArray(final int size) {
            return new AccountMoneyMetadata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (invested ? 1 : 0));
        ParcelableUtil.write(dest, balance);
    }
}

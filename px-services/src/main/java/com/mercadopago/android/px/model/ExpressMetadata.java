package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.ExpressPaymentMethod;
import java.io.Serializable;

public final class ExpressMetadata implements Parcelable, Serializable, ExpressPaymentMethod {

    private final String paymentMethodId;
    private final String paymentTypeId;
    private final CardMetadata card;
    private final AccountMoneyMetadata accountMoney;

    @Override
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    @Override
    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    @Override
    public CardMetadata getCard() {
        return card;
    }

    public AccountMoneyMetadata getAccountMoney() {
        return accountMoney;
    }

    public boolean isAccountMoney() {
        return accountMoney != null;
    }

    @Override
    public boolean isCard() {
        return card != null;
    }

    protected ExpressMetadata(final Parcel in) {
        paymentMethodId = in.readString();
        paymentTypeId = in.readString();
        card = in.readParcelable(CardMetadata.class.getClassLoader());
        accountMoney = in.readParcelable(AccountMoneyMetadata.class.getClassLoader());
    }

    public static final Creator<ExpressMetadata> CREATOR = new Creator<ExpressMetadata>() {
        @Override
        public ExpressMetadata createFromParcel(final Parcel in) {
            return new ExpressMetadata(in);
        }

        @Override
        public ExpressMetadata[] newArray(final int size) {
            return new ExpressMetadata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(paymentMethodId);
        dest.writeString(paymentTypeId);
        dest.writeParcelable(card, flags);
        dest.writeParcelable(accountMoney, flags);
    }
}

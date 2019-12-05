package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.AccountMoneyMetadata;
import com.mercadopago.android.px.model.StatusMetadata;

public class AccountMoneyDrawableFragmentItem extends DrawableFragmentItem {

    @NonNull public final AccountMoneyMetadata metadata;

    public static final Creator<AccountMoneyDrawableFragmentItem> CREATOR =
        new Creator<AccountMoneyDrawableFragmentItem>() {
            @Override
            public AccountMoneyDrawableFragmentItem createFromParcel(final Parcel in) {
                return new AccountMoneyDrawableFragmentItem(in);
            }

            @Override
            public AccountMoneyDrawableFragmentItem[] newArray(final int size) {
                return new AccountMoneyDrawableFragmentItem[size];
            }
        };

    public AccountMoneyDrawableFragmentItem(@NonNull final AccountMoneyMetadata metadata,
        @NonNull final String paymentMethodId, final String highlightMessage, @NonNull final StatusMetadata status) {
        super(paymentMethodId, highlightMessage, status);
        this.metadata = metadata;
    }

    protected AccountMoneyDrawableFragmentItem(final Parcel in) {
        super(in);
        metadata = in.readParcelable(AccountMoneyMetadata.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(metadata, flags);
    }

    @Override
    public Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer) {
        return drawer.draw(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
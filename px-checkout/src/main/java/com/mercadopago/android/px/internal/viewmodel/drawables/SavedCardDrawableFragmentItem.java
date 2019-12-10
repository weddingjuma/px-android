package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.internal.viewmodel.CardDrawerConfiguration;
import com.mercadopago.android.px.model.Reimbursement;
import com.mercadopago.android.px.model.StatusMetadata;

public class SavedCardDrawableFragmentItem extends DrawableFragmentItem {

    @NonNull public final String paymentMethodId;
    @NonNull public final CardDrawerConfiguration card;

    public static final Creator<SavedCardDrawableFragmentItem> CREATOR = new Creator<SavedCardDrawableFragmentItem>() {
        @Override
        public SavedCardDrawableFragmentItem createFromParcel(final Parcel in) {
            return new SavedCardDrawableFragmentItem(in);
        }

        @Override
        public SavedCardDrawableFragmentItem[] newArray(final int size) {
            return new SavedCardDrawableFragmentItem[size];
        }
    };

    public SavedCardDrawableFragmentItem(@NonNull final String paymentMethodId,
        @NonNull final CardDrawerConfiguration card, @NonNull final String cardId,
        @Nullable final String chargeMessage, @NonNull final StatusMetadata status,
        @Nullable final Reimbursement reimbursement) {
        super(cardId, chargeMessage, status, reimbursement);
        this.paymentMethodId = paymentMethodId;
        this.card = card;
    }

    protected SavedCardDrawableFragmentItem(final Parcel in) {
        super(in);
        paymentMethodId = in.readString();
        card = in.readParcelable(CardDrawerConfiguration.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(paymentMethodId);
        dest.writeParcelable(card, flags);
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
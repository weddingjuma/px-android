package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.Reimbursement;
import com.mercadopago.android.px.model.StatusMetadata;
import java.io.Serializable;

public abstract class DrawableFragmentItem implements Parcelable, Serializable {

    private final String id;
    private final String chargeMessage;
    private final StatusMetadata status;
    private final Reimbursement reimbursement;

    protected DrawableFragmentItem(@NonNull final String id, @Nullable final String chargeMessage,
        @NonNull final StatusMetadata status, @Nullable final Reimbursement reimbursement) {
        this.id = id;
        this.chargeMessage = chargeMessage;
        this.status = status;
        this.reimbursement = reimbursement;
    }

    protected DrawableFragmentItem(final Parcel in) {
        id = in.readString();
        chargeMessage = in.readString();
        status = in.readParcelable(StatusMetadata.class.getClassLoader());
        reimbursement = in.readParcelable(Reimbursement.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(chargeMessage);
        dest.writeParcelable(status, flags);
        dest.writeParcelable(reimbursement, flags);
    }

    public abstract Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer);

    public String getId() {
        return id;
    }

    @Nullable
    public String getChargeMessage() {
        return chargeMessage;
    }

    public StatusMetadata getStatus() {
        return status;
    }

    public Reimbursement getReimbursement() {
        return reimbursement;
    }
}
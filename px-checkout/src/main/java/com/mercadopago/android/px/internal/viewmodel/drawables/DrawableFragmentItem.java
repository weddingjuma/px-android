package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.Reimbursement;
import com.mercadopago.android.px.model.StatusMetadata;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import com.mercadopago.android.px.model.internal.Text;
import java.io.Serializable;

public abstract class DrawableFragmentItem implements Parcelable, Serializable {

    private final String id;
    private final StatusMetadata status;
    private final Text bottomDescription;
    private final String chargeMessage;
    private final Reimbursement reimbursement;
    private final DisabledPaymentMethod disabledPaymentMethod;

    protected DrawableFragmentItem(@NonNull final Parameters parameters) {
        id = parameters.id;
        status = parameters.status;
        bottomDescription = parameters.bottomDescription;
        chargeMessage = parameters.chargeMessage;
        reimbursement = parameters.reimbursement;
        disabledPaymentMethod = parameters.disabledPaymentMethod;
    }

    protected DrawableFragmentItem(final Parcel in) {
        id = in.readString();
        status = in.readParcelable(StatusMetadata.class.getClassLoader());
        bottomDescription = in.readParcelable(Text.class.getClassLoader());
        chargeMessage = in.readString();
        reimbursement = in.readParcelable(Reimbursement.class.getClassLoader());
        disabledPaymentMethod = in.readParcelable(DisabledPaymentMethod.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeParcelable(status, flags);
        dest.writeParcelable(bottomDescription, flags);
        dest.writeString(chargeMessage);
        dest.writeParcelable(reimbursement, flags);
        dest.writeParcelable(disabledPaymentMethod, flags);
    }

    public abstract Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer);

    public String getId() {
        return id;
    }

    public StatusMetadata getStatus() {
        return status;
    }

    @Nullable
    public Text getBottomDescription() {
        return bottomDescription;
    }

    @Nullable
    public String getChargeMessage() {
        return chargeMessage;
    }

    @Nullable
    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    @Nullable
    public DisabledPaymentMethod getDisabledPaymentMethod() {
        return disabledPaymentMethod;
    }

    public boolean shouldHighlightBottomDescription() {
        return bottomDescription == null;
    }

    /* default */ static final class Parameters {
        /* default */ @NonNull final String id;
        /* default */ @NonNull final StatusMetadata status;
        /* default */ @Nullable private Text bottomDescription;
        /* default */ @Nullable final String chargeMessage;
        /* default */ @Nullable final Reimbursement reimbursement;
        /* default */ @Nullable final DisabledPaymentMethod disabledPaymentMethod;

        /* default */ Parameters(@NonNull final String id, @NonNull final StatusMetadata status,
            @Nullable final Text bottomDescription, @Nullable final String chargeMessage,
            @Nullable final Reimbursement reimbursement, @Nullable final DisabledPaymentMethod disabledPaymentMethod) {
            this.id = id;
            this.status = status;
            this.bottomDescription = bottomDescription;
            this.chargeMessage = chargeMessage;
            this.reimbursement = reimbursement;
            this.disabledPaymentMethod = disabledPaymentMethod;
        }
    }
}
package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.Reimbursement;
import com.mercadopago.android.px.model.StatusMetadata;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import java.io.Serializable;

public abstract class DrawableFragmentItem implements Parcelable, Serializable {

    private final String id;
    private final String chargeMessage;
    private final StatusMetadata status;
    private final Reimbursement reimbursement;
    private final DisabledPaymentMethod disabledPaymentMethod;
    private String description;
    private String issuerName;

    protected DrawableFragmentItem(@NonNull final Parameters parameters) {
        id = parameters.id;
        chargeMessage = parameters.chargeMessage;
        status = parameters.status;
        reimbursement = parameters.reimbursement;
        disabledPaymentMethod = parameters.disabledPaymentMethod;
        description = parameters.description;
        issuerName = parameters.issuerName;
    }

    protected DrawableFragmentItem(final Parcel in) {
        id = in.readString();
        chargeMessage = in.readString();
        status = in.readParcelable(StatusMetadata.class.getClassLoader());
        reimbursement = in.readParcelable(Reimbursement.class.getClassLoader());
        disabledPaymentMethod = in.readParcelable(DisabledPaymentMethod.class.getClassLoader());
        description = in.readString();
        issuerName = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(chargeMessage);
        dest.writeParcelable(status, flags);
        dest.writeParcelable(reimbursement, flags);
        dest.writeParcelable(disabledPaymentMethod, flags);
        dest.writeString(description);
        dest.writeString(issuerName);
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

    @Nullable
    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    @Nullable
    public DisabledPaymentMethod getDisabledPaymentMethod() {
        return disabledPaymentMethod;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getIssuerName() {
        return issuerName;
    }

    /* default */ static final class Parameters {
        /* default */ @NonNull final String id;
        /* default */ @NonNull final StatusMetadata status;
        /* default */ @Nullable final String chargeMessage;
        /* default */ @Nullable final Reimbursement reimbursement;
        /* default */ @Nullable final DisabledPaymentMethod disabledPaymentMethod;
        /* default */ @NonNull final String description;
        /* default */ @NonNull final String issuerName;

        /* default */ Parameters(@NonNull final String id, @Nullable final String chargeMessage,
            @NonNull final StatusMetadata status, @Nullable final Reimbursement reimbursement,
            @Nullable final DisabledPaymentMethod disabledPaymentMethod, @NonNull final String description,
            @NonNull final String issuerName) {
            this.id = id;
            this.chargeMessage = chargeMessage;
            this.status = status;
            this.reimbursement = reimbursement;
            this.disabledPaymentMethod = disabledPaymentMethod;
            this.description = description;
            this.issuerName = issuerName;
        }
    }
}
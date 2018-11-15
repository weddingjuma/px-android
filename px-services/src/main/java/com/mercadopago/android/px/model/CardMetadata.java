package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CardMetadata implements Parcelable, Serializable {

    public final String id;
    @SerializedName("selected_payer_cost_index") public final int defaultPayerCostIndex;
    public final List<PayerCost> payerCosts;
    public final CardDisplayInfo displayInfo;

    protected CardMetadata(Parcel in) {
        id = in.readString();
        defaultPayerCostIndex = in.readInt();
        payerCosts = in.createTypedArrayList(PayerCost.CREATOR);
        displayInfo = in.readParcelable(CardDisplayInfo.class.getClassLoader());
    }

    public static final Creator<CardMetadata> CREATOR = new Creator<CardMetadata>() {
        @Override
        public CardMetadata createFromParcel(Parcel in) {
            return new CardMetadata(in);
        }

        @Override
        public CardMetadata[] newArray(int size) {
            return new CardMetadata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeInt(defaultPayerCostIndex);
        dest.writeTypedList(payerCosts);
        dest.writeParcelable(displayInfo, flags);
    }

    public PayerCost getPayerCost(final int userSelectedPayerCost) {
        if (userSelectedPayerCost == -1) {
            return payerCosts.get(defaultPayerCostIndex);
        } else {
            return payerCosts.get(userSelectedPayerCost);
        }
    }
}

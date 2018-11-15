package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CardMetadata implements Parcelable, Serializable {

    private String id;
    @SerializedName("selected_payer_cost_index") private int defaultPayerCostIndex;
    private List<PayerCost> payerCosts;
    private CardDisplayInfo displayInfo;

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

    public String getId() {
        return id;
    }

    public int getDefaultPayerCostIndex() {
        return defaultPayerCostIndex;
    }

    public List<PayerCost> getPayerCosts() {
        return payerCosts;
    }

    public CardDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public PayerCost getPayerCost(final int userSelectedPayerCost) {
        if (userSelectedPayerCost == -1) {
            return payerCosts.get(defaultPayerCostIndex);
        } else {
            return payerCosts.get(userSelectedPayerCost);
        }
    }
}

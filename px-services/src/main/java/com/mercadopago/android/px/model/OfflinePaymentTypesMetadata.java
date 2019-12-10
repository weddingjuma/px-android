package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.model.internal.Text;
import java.io.Serializable;
import java.util.List;

public final class OfflinePaymentTypesMetadata implements Parcelable, Serializable {

    private final Text label;
    private final List<OfflinePaymentType> offlinePaymentTypes;

    public static final Creator<OfflinePaymentTypesMetadata> CREATOR = new Creator<OfflinePaymentTypesMetadata>() {
        @Override
        public OfflinePaymentTypesMetadata createFromParcel(final Parcel in) {
            return new OfflinePaymentTypesMetadata(in);
        }

        @Override
        public OfflinePaymentTypesMetadata[] newArray(final int size) {
            return new OfflinePaymentTypesMetadata[size];
        }
    };

    protected OfflinePaymentTypesMetadata(final Parcel in) {
        label = in.readParcelable(Text.class.getClassLoader());
        offlinePaymentTypes = in.createTypedArrayList(OfflinePaymentType.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(label, flags);
        dest.writeTypedList(offlinePaymentTypes);
    }
}

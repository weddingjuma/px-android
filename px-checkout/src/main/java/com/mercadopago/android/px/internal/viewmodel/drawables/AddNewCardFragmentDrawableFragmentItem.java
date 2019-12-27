package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.NewCardMetadata;

public class AddNewCardFragmentDrawableFragmentItem extends DrawableFragmentItem {

    @NonNull public final NewCardMetadata metadata;

    public static final Creator<AddNewCardFragmentDrawableFragmentItem> CREATOR =
        new Creator<AddNewCardFragmentDrawableFragmentItem>() {
            @Override
            public AddNewCardFragmentDrawableFragmentItem createFromParcel(final Parcel in) {
                return new AddNewCardFragmentDrawableFragmentItem(in);
            }

            @Override
            public AddNewCardFragmentDrawableFragmentItem[] newArray(final int size) {
                return new AddNewCardFragmentDrawableFragmentItem[size];
            }
        };

    public AddNewCardFragmentDrawableFragmentItem(@NonNull final Parameters parameters,
        @NonNull final NewCardMetadata metadata) {
        super(parameters);
        this.metadata = metadata;
    }

    protected AddNewCardFragmentDrawableFragmentItem(final Parcel in) {
        super(in);
        metadata = in.readParcelable(NewCardMetadata.class.getClassLoader());
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
package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.model.StatusMetadata;
import java.io.Serializable;

public abstract class DrawableFragmentItem implements Parcelable, Serializable {

    private final String id;
    private final String highlightMessage;
    private final StatusMetadata status;

    protected DrawableFragmentItem(@NonNull final String id, @Nullable final String highlightMessage,
        @NonNull StatusMetadata status) {
        this.id = id;
        this.highlightMessage = highlightMessage;
        this.status = status;
    }

    protected DrawableFragmentItem(final Parcel in) {
        id = in.readString();
        highlightMessage = in.readString();
        status = in.readParcelable(StatusMetadata.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(id);
        dest.writeString(highlightMessage);
        dest.writeParcelable(status, flags);
    }

    public abstract Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer);

    public String getId() {
        return id;
    }

    @Nullable
    public String getHighlightMessage() {
        return highlightMessage;
    }

    public StatusMetadata getStatus() {
        return status;
    }
}
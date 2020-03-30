package com.mercadopago.android.px.model.display_info;

import android.os.Parcel;
import android.os.Parcelable;
import com.mercadopago.android.px.internal.util.ParcelableUtil;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LinkableText implements Parcelable, Serializable {

    private final String text;
    private final String textColor;
    private final List<LinkablePhrase> linkablePhrases;
    private final Map<String, String> links;

    public List<LinkablePhrase> getLinkablePhrases() {
        return linkablePhrases != null ? linkablePhrases : Collections.emptyList();
    }

    public String getText() {
        return text;
    }

    public String getTextColor() {
        return textColor;
    }

    public Map<String, String> getLinks() {
        return links != null ? links : Collections.emptyMap();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(text);
        dest.writeString(textColor);
        dest.writeTypedList(linkablePhrases);
        ParcelableUtil.writeSerializableMap(dest, links != null ? links : Collections.emptyMap());
    }

    protected LinkableText(final Parcel in) {
        text = in.readString();
        textColor = in.readString();
        linkablePhrases = in.createTypedArrayList(LinkablePhrase.CREATOR);
        links = new HashMap<>();
        ParcelableUtil.readSerializableMap(links, in, String.class, String.class);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LinkableText> CREATOR = new Creator<LinkableText>() {
        @Override
        public LinkableText createFromParcel(final Parcel in) {
            return new LinkableText(in);
        }

        @Override
        public LinkableText[] newArray(final int size) {
            return new LinkableText[size];
        }
    };
}
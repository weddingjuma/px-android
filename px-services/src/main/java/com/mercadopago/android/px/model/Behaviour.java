package com.mercadopago.android.px.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class Behaviour implements Parcelable {

    private final String modal;

    public static final Creator<Behaviour> CREATOR = new Creator<Behaviour>() {
        @Override
        public Behaviour createFromParcel(final Parcel in) {
            return new Behaviour(in);
        }

        @Override
        public Behaviour[] newArray(final int size) {
            return new Behaviour[size];
        }
    };

    @SuppressWarnings({ "WeakerAccess", "ProtectedMemberInFinalClass" })
    protected Behaviour(final Parcel in) {
        modal = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(modal);
    }

    public String getModal() {
        return modal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Retention(SOURCE)
    @StringDef({ BehaviourType.SWITCH_SPLIT, BehaviourType.TAP_CARD, BehaviourType.TAP_PAY })
    public @interface BehaviourType {
        String SWITCH_SPLIT = "switch_split";
        String TAP_CARD = "tap_card";
        String TAP_PAY = "tap_pay";
    }
}
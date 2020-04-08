package com.mercadopago.android.px.internal.viewmodel;

import android.os.Parcel;
import android.support.annotation.NonNull;

public class UserValidationPostPaymentAction extends PostPaymentAction {

    public UserValidationPostPaymentAction() {
        super(RequiredAction.USER_VALIDATION);
    }

    /* default */ UserValidationPostPaymentAction(final Parcel in) {
        super(in);
    }

    @Override
    public void execute(@NonNull final ActionController actionController) {
        actionController.onUserValidation();
    }

    public static final Creator<UserValidationPostPaymentAction> CREATOR =
        new Creator<UserValidationPostPaymentAction>() {
            @Override
            public UserValidationPostPaymentAction createFromParcel(final Parcel in) {
                return new UserValidationPostPaymentAction(in);
            }

            @Override
            public UserValidationPostPaymentAction[] newArray(final int size) {
                return new UserValidationPostPaymentAction[size];
            }
        };
}
package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import java.math.BigDecimal;

public class AccountMoneyInfo extends ExtraInfo {

    private BigDecimal balance;

    public AccountMoneyInfo(@NonNull final BigDecimal balance) {
        this.balance = balance;
    }
}

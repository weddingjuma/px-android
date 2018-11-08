package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import java.io.Serializable;

public interface DrawableFragmentItem extends Serializable {

    Fragment draw(@NonNull final PaymentMethodFragmentDrawer drawer);
}

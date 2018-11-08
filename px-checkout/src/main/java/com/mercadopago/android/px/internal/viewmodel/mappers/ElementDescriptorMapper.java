package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.view.ElementDescriptorView;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.preferences.CheckoutPreference;

public class ElementDescriptorMapper extends Mapper<CheckoutPreference, ElementDescriptorView.Model> {

    @Override
    public ElementDescriptorView.Model map(@NonNull final CheckoutPreference checkoutPreference) {
        final Item firstItem = checkoutPreference.getItems().get(0);
        final String title =
            TextUtil.isEmpty(firstItem.getDescription()) ? firstItem.getTitle() : firstItem.getDescription();
        return new ElementDescriptorView.Model(title, firstItem.getPictureUrl(), R.drawable.px_review_item_default);
    }
}

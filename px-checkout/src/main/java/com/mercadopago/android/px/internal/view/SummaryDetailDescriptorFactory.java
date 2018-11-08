package com.mercadopago.android.px.internal.view;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.DiscountAmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.DiscountDescriptionLocalized;
import com.mercadopago.android.px.internal.viewmodel.DiscountDetailColor;
import com.mercadopago.android.px.internal.viewmodel.DiscountDetailDrawable;
import com.mercadopago.android.px.internal.viewmodel.ItemDetailColor;
import com.mercadopago.android.px.internal.viewmodel.ItemLocalized;
import java.util.ArrayList;
import java.util.List;

public class SummaryDetailDescriptorFactory {

    @NonNull private final DiscountRepository discountRepository;
    @NonNull private final PaymentSettingRepository configuration;

    public SummaryDetailDescriptorFactory(@NonNull final DiscountRepository discountRepository,
        @NonNull final PaymentSettingRepository configuration) {
        this.discountRepository = discountRepository;
        this.configuration = configuration;
    }

    public List<AmountDescriptorView.Model> create() {
        final List<AmountDescriptorView.Model> list = new ArrayList<>();

        if (discountRepository.getDiscount() != null) {
            list.add(new AmountDescriptorView.Model(
                new ItemLocalized(),
                new AmountLocalized(configuration.getCheckoutPreference().getTotalAmount(),
                    configuration.getCheckoutPreference().getSite().getCurrencyId()),
                new ItemDetailColor()));
            list.add(new AmountDescriptorView.Model(
                new DiscountDescriptionLocalized(discountRepository.getDiscount()),
                new DiscountAmountLocalized(discountRepository.getDiscount().getCouponAmount(),
                    configuration.getCheckoutPreference().getSite().getCurrencyId()),
                new DiscountDetailColor()).setDetailDrawable(new DiscountDetailDrawable()).enableListener());
        }
        return list;
    }
}

package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo;
import java.util.Map;

public class AppliedDiscountViewTracker extends ViewTracker {
    private static final String PATH = BASE_VIEW_PATH + PAYMENTS_PATH + "/applied_discount";

    @NonNull private final DiscountRepository discountRepository;

    public AppliedDiscountViewTracker(@NonNull final DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        final Map<String, Object> data = super.getData();
        data.put("discount",
            DiscountInfo.with(discountRepository.getDiscount(), discountRepository.getCampaign(),
                !discountRepository.isNotAvailableDiscount()).toMap());
        return data;
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}

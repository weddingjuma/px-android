package com.mercadopago.android.px.tracking.internal.views;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.tracking.internal.mapper.FromItemToItemInfo;
import com.mercadopago.android.px.tracking.internal.mapper.FromUserSelectionToAvailableMethod;
import com.mercadopago.android.px.tracking.internal.model.DiscountInfo;
import com.mercadopago.android.px.tracking.internal.model.ReviewAndConfirmData;
import java.util.Map;
import java.util.Set;

public class ReviewAndConfirmViewTracker extends ViewTracker {

    private static final String PATH = BASE_VIEW_PATH + "/review/traditional";

    private final Set<String> escCardIds;
    @NonNull private final UserSelectionRepository userSelectionRepository;
    @NonNull private final PaymentSettingRepository paymentSettings;
    @NonNull private final DiscountRepository discountRepository;

    public ReviewAndConfirmViewTracker(final Set<String> escCardIds,
        @NonNull final UserSelectionRepository userSelectionRepository,
        @NonNull final PaymentSettingRepository paymentSettings,
        @NonNull final DiscountRepository discountRepository) {
        this.escCardIds = escCardIds;
        this.userSelectionRepository = userSelectionRepository;
        this.paymentSettings = paymentSettings;
        this.discountRepository = discountRepository;
    }

    @NonNull
    @Override
    public Map<String, Object> getData() {
        // given scenarios of recovery failure this protection is needed.
        try {
            return new ReviewAndConfirmData(new FromUserSelectionToAvailableMethod(escCardIds)
                .map(userSelectionRepository),
                new FromItemToItemInfo()
                    .map(paymentSettings.getCheckoutPreference().getItems()),
                paymentSettings.getCheckoutPreference().getTotalAmount(),
                DiscountInfo.with(discountRepository.getDiscount(), discountRepository.getCampaign(),
                    !discountRepository.isNotAvailableDiscount()))
                .toMap();
        } catch (final Exception e) {
            return super.getData();
        }
    }

    @NonNull
    @Override
    public String getViewPath() {
        return PATH;
    }
}

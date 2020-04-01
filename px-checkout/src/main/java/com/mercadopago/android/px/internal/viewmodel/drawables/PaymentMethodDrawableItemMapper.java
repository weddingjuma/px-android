package com.mercadopago.android.px.internal.viewmodel.drawables;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import com.mercadopago.android.px.internal.repository.DisabledPaymentMethodRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.viewmodel.CardDrawerConfiguration;
import com.mercadopago.android.px.internal.viewmodel.DisableConfiguration;
import com.mercadopago.android.px.internal.viewmodel.mappers.NonNullMapper;
import com.mercadopago.android.px.model.CustomSearchItem;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.commission.PaymentTypeChargeRule;
import com.mercadopago.android.px.model.internal.Text;
import java.util.Collections;
import java.util.List;

public class PaymentMethodDrawableItemMapper extends NonNullMapper<ExpressMetadata, DrawableFragmentItem> {

    @NonNull private final DisabledPaymentMethodRepository disabledPaymentMethodRepository;
    @NonNull private final DisableConfiguration disableConfiguration;
    @NonNull private final ChargeRepository chargeRepository;
    @NonNull private List<CustomSearchItem> customSearchItems = Collections.emptyList();

    public PaymentMethodDrawableItemMapper(@NonNull final Context context,
        @NonNull final DisabledPaymentMethodRepository disabledPaymentMethodRepository,
        @NonNull final ChargeRepository chargeRepository) {
        this.disabledPaymentMethodRepository = disabledPaymentMethodRepository;
        disableConfiguration = new DisableConfiguration(context);
        this.chargeRepository = chargeRepository;
    }

    public void setCustomSearchItems(@NonNull final List<CustomSearchItem> items) {
        this.customSearchItems = items;
    }

    @Nullable
    private CustomSearchItem getCustomSearchItemById(@NonNull final String paymentMethodId) {
        for (final CustomSearchItem customSearchItem : customSearchItems) {
            if (paymentMethodId.equals(customSearchItem.getId())) {
                return customSearchItem;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public DrawableFragmentItem map(@NonNull final ExpressMetadata expressMetadata) {
        final DrawableFragmentItem.Parameters parameters = getParameters(expressMetadata);
        if (expressMetadata.isCard()) {
            return new SavedCardDrawableFragmentItem(parameters, expressMetadata.getPaymentMethodId(),
                new CardDrawerConfiguration(expressMetadata.getCard().getDisplayInfo(), disableConfiguration));
        } else if (expressMetadata.isAccountMoney()) {
            return new AccountMoneyDrawableFragmentItem(parameters);
        } else if (expressMetadata.isConsumerCredits()) {
            return new ConsumerCreditsDrawableFragmentItem(parameters, expressMetadata.getConsumerCredits());
        } else if (expressMetadata.isNewCard() || expressMetadata.isOfflineMethods()) {
            return new OtherPaymentMethodFragmentItem(parameters, expressMetadata.getNewCard(),
                expressMetadata.getOfflineMethods());
        }
        return null;
    }

    @NonNull
    private DrawableFragmentItem.Parameters getParameters(@NonNull final ExpressMetadata expressMetadata) {
        final PaymentTypeChargeRule charge = chargeRepository.getChargeRule(expressMetadata.getPaymentTypeId());

        final String customOptionId = expressMetadata.getCustomOptionId();
        String description = TextUtil.EMPTY;
        String issuerName = TextUtil.EMPTY;
        CustomSearchItem item;
        if((item = getCustomSearchItemById(expressMetadata.getCustomOptionId())) != null) {
            description = item.getDescription();
            if (item.getIssuer() != null && item.getIssuer() != null) {
                issuerName = item.getIssuer().getName();
            }
        }

        return new DrawableFragmentItem.Parameters(
            customOptionId,
            charge != null ? charge.getMessage() : null,
            expressMetadata.getStatus(),
            expressMetadata.getBenefits() != null ? expressMetadata.getBenefits().getReimbursement() : null,
            disabledPaymentMethodRepository.getDisabledPaymentMethods().get(customOptionId), description, issuerName
        );
    }
}
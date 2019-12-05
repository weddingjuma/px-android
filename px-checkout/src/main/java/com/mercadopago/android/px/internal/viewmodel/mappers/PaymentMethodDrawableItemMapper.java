package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.repository.ChargeRepository;
import com.mercadopago.android.px.internal.repository.AmountConfigurationRepository;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.viewmodel.CardDrawerConfiguration;
import com.mercadopago.android.px.internal.viewmodel.DisableConfiguration;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMoneyDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.ConsumerCreditsDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableFragmentItem;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.commission.PaymentTypeChargeRule;
import com.mercadopago.android.px.model.internal.Text;

public class PaymentMethodDrawableItemMapper extends NonNullMapper<ExpressMetadata, DrawableFragmentItem> {

    @NonNull final DisableConfiguration disableConfiguration;
    @NonNull private ChargeRepository chargeRepository;
    @NonNull private AmountConfigurationRepository amountConfigurationRepository;

    public PaymentMethodDrawableItemMapper(@NonNull final Context context,
        @NonNull final ChargeRepository chargeRepository,
        @NonNull final AmountConfigurationRepository amountConfigurationRepository) {
        disableConfiguration = new DisableConfiguration(context);
        this.chargeRepository = chargeRepository;
        this.amountConfigurationRepository = amountConfigurationRepository;
    }

    @Override
    public DrawableFragmentItem map(@NonNull final ExpressMetadata expressMetadata) {
        final PaymentTypeChargeRule charge = chargeRepository.getChargeRule(expressMetadata.getPaymentTypeId());
        final String highlightMessage = charge != null ? charge.getMessage() : getReimbursementText(expressMetadata);

        if (expressMetadata.isCard()) {
            return new SavedCardDrawableFragmentItem(expressMetadata.getPaymentMethodId(),
                new CardDrawerConfiguration(expressMetadata.getCard().getDisplayInfo(), disableConfiguration),
                expressMetadata.getCard().getId(), highlightMessage, expressMetadata.getStatus());
        } else if (PaymentTypes.isAccountMoney(expressMetadata.getPaymentMethodId())) {
            return new AccountMoneyDrawableFragmentItem(expressMetadata.getAccountMoney(),
                expressMetadata.getPaymentMethodId(), highlightMessage, expressMetadata.getStatus());
        } else if (expressMetadata.isConsumerCredits()) {
            return new ConsumerCreditsDrawableFragmentItem(expressMetadata.getConsumerCredits(),
                expressMetadata.getPaymentMethodId(), highlightMessage, expressMetadata.getStatus());
        } else if (expressMetadata.isNewCard()) {
            return new AddNewCardFragmentDrawableFragmentItem(expressMetadata.getPaymentMethodId(),
                expressMetadata.getStatus(), expressMetadata.getNewCard());
        }
        return null;
    }

    private String getReimbursementText(@NonNull final ExpressMetadata expressMetadata) {
        if (expressMetadata.isNewCard() || !expressMetadata.getStatus().isEnabled()) {
            return null;
        }
        final AmountConfiguration configuration =
            amountConfigurationRepository.getConfigurationFor(expressMetadata.getCustomOptionId());
        for (final PayerCost payerCost : configuration.getPayerCosts()) {
            final Text reimbursement = payerCost.getReimbursement();
            if (reimbursement != null && TextUtil.isNotEmpty(reimbursement.getMessage())) {
                return reimbursement.getMessage();
            }
        }
        return null;
    }
}
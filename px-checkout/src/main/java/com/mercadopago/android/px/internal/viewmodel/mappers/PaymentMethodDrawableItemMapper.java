package com.mercadopago.android.px.internal.viewmodel.mappers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.viewmodel.CardDrawerConfiguration;
import com.mercadopago.android.px.internal.viewmodel.DisableConfiguration;
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMoneyDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.AddNewCardFragmentDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.ConsumerCreditsDrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableFragmentItem;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.PaymentTypes;

public class PaymentMethodDrawableItemMapper extends NonNullMapper<ExpressMetadata, DrawableFragmentItem> {

    @NonNull final DisableConfiguration disableConfiguration;

    public PaymentMethodDrawableItemMapper(@NonNull final Context context) {
        disableConfiguration = new DisableConfiguration(context);
    }

    @Override
    public DrawableFragmentItem map(@NonNull final ExpressMetadata expressMetadata) {
        if (expressMetadata.isCard()) {
            return new SavedCardDrawableFragmentItem(expressMetadata.getPaymentMethodId(),
                new CardDrawerConfiguration(expressMetadata.getCard().getDisplayInfo(), disableConfiguration),
                expressMetadata.getCard().getId(), expressMetadata.getStatus());
        } else if (PaymentTypes.isAccountMoney(expressMetadata.getPaymentMethodId())) {
            return new AccountMoneyDrawableFragmentItem(expressMetadata.getAccountMoney(),
                expressMetadata.getPaymentMethodId(), expressMetadata.getStatus());
        } else if (expressMetadata.isConsumerCredits()) {
            return new ConsumerCreditsDrawableFragmentItem(expressMetadata.getConsumerCredits(),
                expressMetadata.getPaymentMethodId(), expressMetadata.getStatus());
        } else if (expressMetadata.isNewCard()) {
            return new AddNewCardFragmentDrawableFragmentItem(expressMetadata.getPaymentMethodId(),
                expressMetadata.getStatus(), expressMetadata.getNewCard());
        }

        return null;
    }
}
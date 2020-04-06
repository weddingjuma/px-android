package com.mercadopago.android.px.internal.features.express.slider;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.meli.android.carddrawer.model.CardDrawerView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.viewmodel.drawables.SavedCardDrawableFragmentItem;

public class SavedCardFragment extends PaymentMethodFragment<SavedCardDrawableFragmentItem> {

    private CardDrawerView cardView;

    @NonNull
    public static Fragment getInstance(final SavedCardDrawableFragmentItem model) {
        final SavedCardFragment instance = new SavedCardFragment();
        instance.storeModel(model);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_saved_card, container, false);
    }

    @Override
    public void initializeViews(@NonNull final View view) {
        super.initializeViews(view);
        cardView = view.findViewById(R.id.card);

        setIssuerIcon(view.getContext(), model);
        setPaymentMethodIcon(view.getContext(), model);

        cardView.getCard().setName(model.card.getName());
        cardView.getCard().setExpiration(model.card.getDate());
        cardView.getCard().setNumber(model.card.getNumber());
        cardView.show(model.card);
        cardView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
    }

    @Override
    protected String getAccessibilityContentDescription() {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        builder
            .append(model.paymentMethodId)
            .append(TextUtil.SPACE)
            .append(model.getIssuerName())
            .append(TextUtil.SPACE)
            .append(model.getDescription())
            .append(TextUtil.SPACE)
            .append(getString(R.string.px_date_divider))
            .append(TextUtil.SPACE)
            .append(model.card.getName());

        return builder.toString();
    }

    protected void setIssuerIcon(@NonNull final Context context,
        @NonNull final SavedCardDrawableFragmentItem drawableCard) {
        final int issuerResource = ResourceUtil.getCardIssuerImage(context, drawableCard.card.getIssuerImageName());

        if (issuerResource > 0) {
            drawableCard.card.setIssuerRes(issuerResource);
        }
    }

    private void setPaymentMethodIcon(@NonNull final Context context,
        @NonNull final SavedCardDrawableFragmentItem drawableCard) {
        final int paymentMethodResource = ResourceUtil.getCardImage(context, drawableCard.paymentMethodId);

        if (paymentMethodResource > 0) {
            drawableCard.card.setLogoRes(paymentMethodResource);
        }
    }

    @Override
    public void disable() {
        super.disable();
        model.card.disable();
        storeModel(model);
        cardView.show(model.card);
    }
}
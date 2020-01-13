package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.express.ExpressPaymentFragment;
import com.mercadopago.android.px.internal.features.payment_vault.PaymentVaultActivity;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.viewmodel.drawables.OtherPaymentMethodFragmentItem;
import com.mercadopago.android.px.model.NewCardMetadata;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;
import com.mercadopago.android.px.model.internal.Text;

public class OtherPaymentMethodFragment
    extends BaseFragment<ChangePaymentMethodPresenter, OtherPaymentMethodFragmentItem>
    implements AddNewCard.View {

    private View addNewCardView;
    private View offPaymentMethodView;

    @NonNull
    public static Fragment getInstance(@NonNull final OtherPaymentMethodFragmentItem model) {
        final OtherPaymentMethodFragment instance = new OtherPaymentMethodFragment();
        instance.storeModel(model);
        return instance;
    }

    @Override
    protected ChangePaymentMethodPresenter createPresenter() {
        return new ChangePaymentMethodPresenter(Session.getInstance().getInitRepository());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        final boolean smallMode = model.getNewCardMetadata() != null && model.getOfflineMethodsMetadata() != null;
        return smallMode ? inflater.inflate(R.layout.px_fragment_other_payment_method_small, container, false) :
            inflater.inflate(R.layout.px_fragment_other_payment_method_large, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addNewCardView = view.findViewById(R.id.px_add_new_card);
        offPaymentMethodView = view.findViewById(R.id.px_off_payment_method);
        if (model.getNewCardMetadata() != null) {
            configureAddNewCard(model.getNewCardMetadata());
        }
        if (model.getOfflineMethodsMetadata() != null) {
            configureOffMethods(model.getOfflineMethodsMetadata());
        }
    }

    private void configureAddNewCard(@NonNull final NewCardMetadata newCardMetadata) {
        addNewCardView.setVisibility(View.VISIBLE);
        configureViews(
            addNewCardView,
            R.drawable.px_ico_new_card,
            newCardMetadata.getLabel(),
            newCardMetadata.getDescription(),
            v -> presenter.onAddNewCardSelected());
    }

    private void configureOffMethods(@NonNull final OfflinePaymentTypesMetadata offlineMethodsMetadata) {
        offPaymentMethodView.setVisibility(View.VISIBLE);
        configureViews(
            offPaymentMethodView,
            R.drawable.px_ico_off_method,
            offlineMethodsMetadata.getLabel(),
            offlineMethodsMetadata.getDescription(),
            v -> {
                final ExpressPaymentFragment parentFragment = ((ExpressPaymentFragment) getParentFragment());
                parentFragment.onBottomButtonClicked();
            });
    }

    private void configureViews(@NonNull final View view, @DrawableRes final int imageResId,
        @NonNull final Text primaryMessage, @Nullable final Text secondaryMessage,
        final View.OnClickListener listener) {
        final MPTextView primaryMessageView = view.findViewById(R.id.other_payment_method_primary_message);
        final MPTextView secondaryMessageView = view.findViewById(R.id.other_payment_method_secondary_message);
        final ImageView image = view.findViewById(R.id.other_payment_method_image);
        ViewUtils.loadOrHide(View.GONE, primaryMessage, primaryMessageView);
        ViewUtils.loadOrHide(View.GONE, secondaryMessage, secondaryMessageView);
        ViewUtils.loadOrGone(imageResId, image);
        view.setOnClickListener(listener);
    }

    @Override
    public void showPaymentMethods() {
        //TODO refactor
        PaymentVaultActivity.start(getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT);
    }

    @Override
    public void showPaymentMethodsWithSelection(@NonNull final PaymentMethodSearchItem paymentMethodSearchItem) {
        //TODO refactor
        PaymentVaultActivity
            .startWithPaymentMethodSelected(getActivity(), CheckoutActivity.REQ_PAYMENT_VAULT, paymentMethodSearchItem);
    }
}
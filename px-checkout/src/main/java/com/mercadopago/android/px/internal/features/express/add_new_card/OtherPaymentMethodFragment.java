package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.ConfigurationModule;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.express.ExpressPaymentFragment;
import com.mercadopago.android.px.internal.features.payment_vault.PaymentVaultActivity;
import com.mercadopago.android.px.internal.util.CardFormWithFragmentWrapper;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.internal.viewmodel.drawables.OtherPaymentMethodFragmentItem;
import com.mercadopago.android.px.model.NewCardMetadata;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import com.mercadopago.android.px.model.PaymentMethodSearchItem;
import com.mercadopago.android.px.model.internal.Text;

public class OtherPaymentMethodFragment
    extends BaseFragment<OtherPaymentMethodPresenter, OtherPaymentMethodFragmentItem>
    implements AddNewCard.View {

    private static final String OLD_VERSION = "v1";

    private View addNewCardView;
    private View offPaymentMethodView;

    @NonNull
    public static Fragment getInstance(@NonNull final OtherPaymentMethodFragmentItem model) {
        final OtherPaymentMethodFragment instance = new OtherPaymentMethodFragment();
        instance.storeModel(model);
        return instance;
    }

    @Override
    protected OtherPaymentMethodPresenter createPresenter() {
        if (model.getNewCardMetadata() != null && OLD_VERSION.equals(model.getNewCardMetadata().getVersion())) {
            return new AddNewCardOldPresenter(Session.getInstance().getInitRepository());
        } else {
            final ConfigurationModule module = Session.getInstance().getConfigurationModule();
            return new OtherPaymentMethodPresenter(
                module.getPaymentSettings(),
                module.getSessionIdProvider());
        }
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

    private void configureOffMethods(@NonNull final OfflinePaymentTypesMetadata offlineMethods) {
        offPaymentMethodView.setVisibility(View.VISIBLE);
        configureViews(
            offPaymentMethodView,
            R.drawable.px_ico_off_method,
            offlineMethods.getLabel(),
            offlineMethods.getDescription(),
            v -> {
                final Fragment parentFragment = getParentFragment();
                if (parentFragment instanceof OnOtherPaymentMethodClickListener) {
                    ((OnOtherPaymentMethodClickListener) parentFragment).onOtherPaymentMethodClicked(offlineMethods);
                } else {
                    throw new IllegalStateException(
                        "Parent fragment must implement " + OnOtherPaymentMethodClickListener.class.getSimpleName());
                }
            });
    }

    private void configureViews(@NonNull final View view, @DrawableRes final int imageResId,
        @NonNull final Text primaryMessage, @Nullable final Text secondaryMessage,
        final View.OnClickListener listener) {
        loadPrimaryMessageView(view, primaryMessage);
        loadSecondaryMessageView(view, secondaryMessage);
        loadImage(view, imageResId);
        view.setOnClickListener(listener);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        final View view = getView();
        final ViewGroup parent = view != null ? (ViewGroup) view.getParent() : null;

        if (presenter != null && parent != null) {
            parent.performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS, null);
            if (isVisibleToUser) {
                offPaymentMethodView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                addNewCardView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            }
        }
    }

    protected void loadPrimaryMessageView(@NonNull final View view, @Nullable final Text primaryMessage) {
        final MPTextView primaryMessageView = view.findViewById(R.id.other_payment_method_primary_message);
        ViewUtils.loadOrHide(View.GONE, primaryMessage, primaryMessageView);
    }

    protected void loadSecondaryMessageView(@NonNull final View view, @Nullable final Text secondaryMessage) {
        final MPTextView secondaryMessageView = view.findViewById(R.id.other_payment_method_secondary_message);
        ViewUtils.loadOrHide(View.GONE, secondaryMessage, secondaryMessageView);
    }

    protected void loadImage(@NonNull final View view, @DrawableRes final int imageResId) {
        final ImageView image = view.findViewById(R.id.other_payment_method_image);
        ViewUtils.loadOrGone(imageResId, image);
    }

    @Override
    public void startCardForm(@NonNull final CardFormWithFragmentWrapper cardFormWithFragmentWrapper) {
        FragmentManager manager;
        if (getParentFragment() != null && (manager = getParentFragment().getFragmentManager()) != null) {
            cardFormWithFragmentWrapper.getCardFormWithFragment()
                .start(manager, ExpressPaymentFragment.REQ_CODE_CARD_FORM, R.id.one_tap_fragment);
        }
    }

    @Override
    public void showPaymentMethods(@Nullable final PaymentMethodSearchItem paymentMethodSearchItem) {
        final Activity activity = getActivity();
        if (activity != null) {
            if (paymentMethodSearchItem == null) {
                PaymentVaultActivity.start(activity, CheckoutActivity.REQ_PAYMENT_VAULT);
            } else {
                PaymentVaultActivity.startWithPaymentMethodSelected(
                    activity, CheckoutActivity.REQ_PAYMENT_VAULT, paymentMethodSearchItem);
            }
        }
    }

    public interface OnOtherPaymentMethodClickListener {
        void onOtherPaymentMethodClicked(@NonNull final OfflinePaymentTypesMetadata offlineMethods);
    }
}
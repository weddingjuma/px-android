package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadolibre.android.ui.widgets.MeliSnackbar;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.features.explode.ExplodeParams;
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment;
import com.mercadopago.android.px.internal.features.plugins.PaymentProcessorActivity;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.FragmentUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class OfflineMethodsFragment extends BaseFragment<OfflineMethodsPresenter, OfflinePaymentTypesMetadata>
    implements OfflineMethods.OffMethodsView, ExplodingFragment.ExplodingAnimationListener {

    private static final String TAG_EXPLODING_FRAGMENT = "TAG_EXPLODING_FRAGMENT";
    private static final int REQ_CODE_PAYMENT_PROCESSOR = 101;

    private MeliButton confirmButton;
    private TextView totalAmountTextView;

    @NonNull
    public static OfflineMethodsFragment getInstance(@NonNull final OfflinePaymentTypesMetadata model) {
        final OfflineMethodsFragment instance = new OfflineMethodsFragment();
        instance.storeModel(model);
        return instance;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
        @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.px_fragment_offline_methods, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onViewResumed();
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setState(MeliButton.State.DISABLED);
        confirmButton.setOnClickListener(v -> {
            if (ApiUtil.checkConnection(getActivity().getApplicationContext())) {
                presenter.startPayment();
            } else {
                presenter.manageNoConnection();
            }
        });
        totalAmountTextView = view.findViewById(R.id.total_amount);
        final ImageView closeImage = view.findViewById(R.id.image);
        closeImage.setOnClickListener(v -> getActivity().onBackPressed());

        configureRecycler(view.findViewById(R.id.methods));

        presenter.loadViewModel();
    }

    private void configureRecycler(@NonNull final RecyclerView recycler) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);
        final DividerItemDecoration decoration =
            new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.px_item_decorator_divider));
        recycler.addItemDecoration(decoration);

        final OnMethodSelectedListener onMethodSelectedListener = selectedItem -> {
            presenter.selectMethod(selectedItem);
            confirmButton.setState(MeliButton.State.NORMAL);
        };

        final OfflineMethodsAdapter offlineMethodsAdapter =
            new OfflineMethodsAdapter(new FromOfflinePaymentTypesMetadataToOfflineItems(getContext()).map(model),
                onMethodSelectedListener);
        recycler.setAdapter(offlineMethodsAdapter);
    }

    @Override
    public void updateTotalView(@NonNull final AmountLocalized amountLocalized) {
        final Editable editable = new SpannableStringBuilder();
        editable.append(getContext().getString(R.string.px_review_summary_total));
        editable.append(TextUtil.SPACE);
        editable.append(amountLocalized.get(getContext()));

        ViewUtils.setFontInSpannable(getContext(), PxFont.SEMI_BOLD, editable);

        totalAmountTextView.setText(editable);
    }

    @Override
    protected OfflineMethodsPresenter createPresenter() {
        final Session session = Session.getInstance();
        return new OfflineMethodsPresenter(session.getPaymentRepository(),
            session.getConfigurationModule().getPaymentSettings(),
            session.getAmountRepository(),
            session.getDiscountRepository(),
            model.getPaymentTypes().get(0).getId());
    }

    @Override
    public void startLoadingButton(final int paymentTimeout, @NonNull final PayButtonViewModel payButtonViewModel) {
        hideConfirmButton();

        ViewUtils.runWhenViewIsFullyMeasured(getView(), () -> {
            final ExplodeParams explodeParams = ExplodingFragment.getParams(confirmButton,
                payButtonViewModel.getButtonProgressText(confirmButton.getContext()), paymentTimeout);
            final FragmentManager childFragmentManager = getChildFragmentManager();
            final ExplodingFragment explodingFragment = ExplodingFragment.newInstance(explodeParams);
            childFragmentManager.beginTransaction()
                .replace(R.id.exploding_frame, explodingFragment, TAG_EXPLODING_FRAGMENT)
                .commitNowAllowingStateLoss();
        });
    }

    private void hideConfirmButton() {
        confirmButton.clearAnimation();
        confirmButton.setVisibility(INVISIBLE);
    }

    private void showConfirmButton() {
        confirmButton.clearAnimation();
        confirmButton.setVisibility(VISIBLE);
    }

    @Override
    public void disableCloseButton() {
        //TODO disabled close button
    }

    @Override
    public void showPaymentResult(final IPaymentDescriptor payment) {
        if (getActivity() != null) {
            ((CheckoutActivity) getActivity()).presenter.onPaymentFinished(payment);
        }
    }

    @Override
    public void onAnimationFinished() {
        presenter.hasFinishPaymentAnimation();
    }

    @Override
    public void finishLoading(@NonNull final ExplodeDecorator params) {
        final ExplodingFragment fragment =
            FragmentUtil.getFragmentByTag(getChildFragmentManager(), TAG_EXPLODING_FRAGMENT, ExplodingFragment.class);
        if (fragment != null) {
            fragment.finishLoading(params);
        } else {
            presenter.hasFinishPaymentAnimation();
        }
    }

    @Override
    public void cancelLoading() {
        showConfirmButton();
        final FragmentManager childFragmentManager = getChildFragmentManager();
        final ExplodingFragment fragment =
            (ExplodingFragment) childFragmentManager.findFragmentByTag(TAG_EXPLODING_FRAGMENT);
        if (fragment != null && fragment.isAdded() && fragment.hasFinished()) {
            childFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitNowAllowingStateLoss();
        }
    }

    @SuppressLint("Range")
    @Override
    public void showErrorSnackBar(@NonNull final MercadoPagoError error) {
        if (getView() != null && getActivity() != null) {
            MeliSnackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG, MeliSnackbar.SnackbarType.ERROR)
                .show();
        }
    }

    @Override
    public void showErrorScreen(@NonNull final MercadoPagoError error) {
        if (getActivity() != null) {
            ((CheckoutActivity) getActivity()).presenter.onPaymentError(error);
        }
    }

    @Override
    public void showPaymentProcessor() {
        PaymentProcessorActivity.start(this, REQ_CODE_PAYMENT_PROCESSOR);
    }

    interface OnMethodSelectedListener {
        void onItemSelected(@NonNull final OfflineMethodItem selectedMethod);
    }

    @Override
    public void onDetach() {
        getActivity().findViewById(R.id.off_methods_fragment).setVisibility(View.GONE);
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDetach();
    }
}
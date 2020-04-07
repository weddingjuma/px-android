package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadolibre.android.ui.widgets.MeliSnackbar;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.addons.BehaviourProvider;
import com.mercadopago.android.px.addons.model.SecurityValidationData;
import com.mercadopago.android.px.core.BackHandler;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.internal.core.ConnectionHelper;
import com.mercadopago.android.px.internal.di.Session;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.features.explode.ExplodeParams;
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment;
import com.mercadopago.android.px.internal.features.plugins.PaymentProcessorActivity;
import com.mercadopago.android.px.internal.font.PxFont;
import com.mercadopago.android.px.internal.util.FragmentUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class OfflineMethodsFragment extends BaseFragment<OfflineMethodsPresenter, OfflinePaymentTypesMetadata>
    implements OfflineMethods.OffMethodsView, ExplodingFragment.ExplodingAnimationListener, BackHandler {

    private static final String TAG_EXPLODING_FRAGMENT = "TAG_EXPLODING_FRAGMENT";
    private static final int REQ_CODE_PAYMENT_PROCESSOR = 201;
    private static final int REQ_CODE_BIOMETRICS = 202;

    @Nullable /* default */ Animation fadeInAnimation;
    @Nullable /* default */ Animation fadeOutAnimation;
    /* default */ View panIndicator;
    private MeliButton confirmButton;
    private TextView totalAmountTextView;
    private View header;

    private int lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
    @Nullable private ExplodingFragment explodingFragment;
    private SheetHandler sheetHandler;

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
        super.onViewCreated(view, savedInstanceState);
        header = view.findViewById(R.id.header);
        panIndicator = view.findViewById(R.id.pan_indicator);
        ViewUtils.loadOrHide(View.GONE,
            model.getDisplayInfo() != null ? model.getDisplayInfo().getBottomDescription() : null,
            view.findViewById(R.id.bottom_description));
        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setState(MeliButton.State.DISABLED);
        confirmButton.setOnClickListener(v -> {
            if (ConnectionHelper.getInstance().checkConnection()) {
                presenter.startSecuredPayment();
            } else {
                presenter.manageNoConnection();
            }
        });
        totalAmountTextView = view.findViewById(R.id.total_amount);
        final ImageView closeImage = view.findViewById(R.id.close);
        closeImage.setOnClickListener(v -> {
            final Activity activity = getActivity();
            if (activity != null) {
                activity.onBackPressed();
            }
        });

        configureRecycler(view.findViewById(R.id.methods));

        if (savedInstanceState == null) {
            presenter.trackOfflineMethodsView(model);
        } else {
            explodingFragment = (ExplodingFragment) getFragmentManager().findFragmentByTag(ExplodingFragment.TAG);
        }

        presenter.updateModel();
    }

    private void configureRecycler(@NonNull final RecyclerView recycler) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final boolean atTop = !recyclerView.canScrollVertically(-1);
                switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    if (atTop) {
                        panIndicator.clearAnimation();
                        panIndicator.startAnimation(fadeOutAnimation);
                    }
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    if (atTop) {
                        panIndicator.clearAnimation();
                        panIndicator.startAnimation(fadeInAnimation);
                    }
                    break;
                default:
                }
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
        final Editable editableDescription = new SpannableStringBuilder();
        final String totalText = getString(R.string.px_review_summary_total);
        editable.append(totalText);
        editable.append(TextUtil.SPACE);
        editable.append(amountLocalized.get(getContext()));

        ViewUtils.setFontInSpannable(getContext(), PxFont.SEMI_BOLD, editable);

        totalAmountTextView.setText(editable);

        editableDescription
            .append(totalText)
            .append(TextUtil.SPACE)
            .append(String.valueOf(amountLocalized.getAmount().floatValue()))
            .append(getString(R.string.px_money));

        totalAmountTextView.setContentDescription(editableDescription);
    }

    @Override
    protected OfflineMethodsPresenter createPresenter() {
        final Session session = Session.getInstance();
        return new OfflineMethodsPresenter(session.getPaymentRepository(),
            session.getConfigurationModule().getPaymentSettings(),
            session.getAmountRepository(),
            session.getDiscountRepository(),
            session.getProductIdProvider(),
            model.getPaymentTypes().isEmpty() ? TextUtil.EMPTY : model.getPaymentTypes().get(0).getId(),
            session.getInitRepository());
    }

    @Override
    public void startLoadingButton(final int paymentTimeout, @NonNull final PayButtonViewModel payButtonViewModel) {
        sheetHandler.setSheetHideable(false);
        hideConfirmButton();
        ViewUtils.runWhenViewIsFullyMeasured(getView(), () -> {
            final ExplodeParams explodeParams = ExplodingFragment.getParams(confirmButton,
                payButtonViewModel.getButtonProgressText(confirmButton.getContext()), paymentTimeout);
            explodingFragment = ExplodingFragment.newInstance(explodeParams);
            explodingFragment.setTargetFragment(this, 0);
            getFragmentManager().beginTransaction()
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
    public void showPaymentResult(final IPaymentDescriptor payment) {
        sheetHandler.onPaymentFinished(payment);
    }

    @Override
    public void onAnimationFinished() {
        presenter.hasFinishPaymentAnimation();
    }

    @Override
    public void finishLoading(@NonNull final ExplodeDecorator params) {
        if (explodingFragment != null) {
            explodingFragment.finishLoading(params);
        } else {
            presenter.hasFinishPaymentAnimation();
        }
        sheetHandler.setSheetHideable(true);
    }

    @Override
    public void cancelLoading() {
        showConfirmButton();
        if (explodingFragment != null && explodingFragment.isAdded() && explodingFragment.hasFinished()) {
            getFragmentManager()
                .beginTransaction()
                .remove(explodingFragment)
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

    @Override
    public boolean handleBack() {
        if (getFragmentManager() != null && !isExploding()) {
            presenter.trackAbort();
        }
        return !isExploding();
    }

    @Override
    public boolean isExploding() {
        return FragmentUtil.isFragmentVisible(getFragmentManager(), TAG_EXPLODING_FRAGMENT);
    }

    interface OnMethodSelectedListener {
        void onItemSelected(@NonNull final OfflineMethodItem selectedMethod);
    }

    @Override
    public Animation onCreateAnimation(final int transit, final boolean enter, final int nextAnim) {
        final int offset = getResources().getInteger(R.integer.px_long_animation_time);
        final int duration = getResources().getInteger(R.integer.px_shorter_animation_time);
        final Animation animation =
            AnimationUtils.loadAnimation(getContext(), enter ? R.anim.px_fade_in : R.anim.px_fade_out);
        animation.setDuration(duration);
        if (enter) {
            animation.setStartOffset(offset);
        }
        header.startAnimation(animation);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onSlideSheet(final float offset) {
        header.setAlpha(offset >= 0 ? offset : 0);
    }

    @Override
    public void onSheetStateChanged(final int newSheetState) {
        lastSheetState = newSheetState;
    }

    @Override
    public void startSecurityValidation(final SecurityValidationData data) {
        confirmButton.setState(MeliButton.State.DISABLED);
        BehaviourProvider.getSecurityBehaviour().startValidation(this, data, REQ_CODE_BIOMETRICS);
    }

    @Override
    public void startKnowYourCustomerFlow(@NonNull final String flowLink) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(flowLink));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQ_CODE_BIOMETRICS) {
            handleBiometricsResult(resultCode);
        }
    }

    private void handleBiometricsResult(final int resultCode) {
        if (resultCode == RESULT_OK) {
            presenter.startPayment();
        } else {
            presenter.trackSecurityFriction();
        }
        confirmButton.setState(MeliButton.State.NORMAL);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        final int duration = getResources().getInteger(R.integer.px_shorter_animation_time);
        fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.px_fade_in);
        fadeInAnimation.setDuration(duration);
        fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.px_fade_out);
        fadeOutAnimation.setDuration(duration);
        final Fragment fragment = getTargetFragment();
        if (fragment instanceof SheetHandler) {
            sheetHandler = (SheetHandler) fragment;
        } else {
            throw new IllegalStateException("Target fragment should implement " + SheetHandler.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fadeInAnimation = null;
        fadeOutAnimation = null;
        sheetHandler = null;
    }

    public interface SheetHandler {
        void setSheetHideable(boolean isHideable);
        //TODO remove when sheet has new pay button implementation
        void onPaymentFinished(@NonNull IPaymentDescriptor payment);
    }
}
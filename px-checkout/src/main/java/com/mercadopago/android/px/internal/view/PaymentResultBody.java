package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mercadolibre.android.mlbusinesscomponents.components.common.dividingline.MLBusinessDividingLineView;
import com.mercadolibre.android.mlbusinesscomponents.components.common.downloadapp.MLBusinessDownloadAppData;
import com.mercadolibre.android.mlbusinesscomponents.components.common.downloadapp.MLBusinessDownloadAppView;
import com.mercadolibre.android.mlbusinesscomponents.components.crossselling.MLBusinessCrossSellingBoxData;
import com.mercadolibre.android.mlbusinesscomponents.components.crossselling.MLBusinessCrossSellingBoxView;
import com.mercadolibre.android.mlbusinesscomponents.components.discount.MLBusinessDiscountBoxData;
import com.mercadolibre.android.mlbusinesscomponents.components.discount.MLBusinessDiscountBoxView;
import com.mercadolibre.android.mlbusinesscomponents.components.loyalty.MLBusinessLoyaltyRingData;
import com.mercadolibre.android.mlbusinesscomponents.components.loyalty.MLBusinessLoyaltyRingView;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.business_result.CongratsViewModel;
import com.mercadopago.android.px.internal.util.FragmentUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.ExternalFragment;
import com.mercadopago.android.px.model.internal.Action;
import com.mercadopago.android.px.model.internal.Text;
import java.util.List;

import static com.mercadopago.android.px.internal.util.MercadoPagoUtil.isMPInstalled;

public final class PaymentResultBody extends LinearLayout {

    public interface Listener extends ActionDispatcher, MLBusinessLoyaltyRingView.OnClickLoyaltyRing,
        MLBusinessDiscountBoxView.OnClickDiscountBox,
        MLBusinessDownloadAppView.OnClickDownloadApp,
        MLBusinessCrossSellingBoxView.OnClickCrossSellingBoxView {

        void onClickShowAllDiscounts(@NonNull final String deepLink);

        void onClickViewReceipt(@NonNull final String deeLink);
    }

    public PaymentResultBody(final Context context) {
        this(context, null);
    }

    public PaymentResultBody(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentResultBody(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.px_payment_result_body, this);
        setOrientation(VERTICAL);
    }

    public void init(@NonNull final Model model, @NonNull final Listener listener) {
        renderTopTextBox(model.congratsViewModel.getTopTextBox());
        renderFragment(R.id.px_fragment_container_important, model.importantFragment);
        renderLoyalty(model.congratsViewModel.getLoyaltyRingData(), listener);
        renderDiscounts(model.congratsViewModel.getDiscountBoxData(), listener);
        renderShowAllDiscounts(model.congratsViewModel.getShowAllDiscounts(), listener);
        renderDownload(model.congratsViewModel.getDownloadAppData(), listener);
        renderCrossSellingBox(model.congratsViewModel.getCrossSellingBoxData(), listener);
        renderReceipt(model.receiptId);
        renderHelp(model.help);
        renderFragment(R.id.px_fragment_container_top, model.topFragment);
        renderMethods(model);
        renderViewReceipt(model.congratsViewModel.getViewReceipt(), listener);
        renderFragment(R.id.px_fragment_container_bottom, model.bottomFragment);
    }

    private void renderTopTextBox(@NonNull final Text topTextBox) {
        final boolean isVisible = ViewUtils.loadOrHide(View.GONE, topTextBox, findViewById(R.id.top_text_box));
        findViewById(R.id.top_text_box_separator).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void renderLoyalty(@Nullable final MLBusinessLoyaltyRingData loyaltyData,
        @NonNull final Listener onClickLoyaltyRing) {
        final MLBusinessLoyaltyRingView loyaltyView = findViewById(R.id.loyaltyView);
        final MLBusinessDividingLineView dividingView = findViewById(R.id.dividingLineView);

        if (loyaltyData != null) {
            loyaltyView.init(loyaltyData, onClickLoyaltyRing);
        } else {
            loyaltyView.setVisibility(GONE);
            dividingView.setVisibility(GONE);
        }
    }

    private void renderShowAllDiscounts(@Nullable final Action showAllDiscountAction,
        @NonNull final Listener onClickDiscountBox) {
        final MeliButton showAllDiscounts = findViewById(R.id.showAllDiscounts);

        if (showAllDiscountAction != null && isMPInstalled(getContext().getPackageManager())) {
            showAllDiscounts.setText(showAllDiscountAction.getLabel());
            showAllDiscounts.setOnClickListener(
                v -> onClickDiscountBox.onClickShowAllDiscounts(showAllDiscountAction.getTarget()));
        } else {
            showAllDiscounts.setVisibility(GONE);
        }
    }

    private void renderDiscounts(@Nullable final MLBusinessDiscountBoxData discountData,
        @NonNull final Listener onClickDiscountBox) {
        final MLBusinessDiscountBoxView discountView = findViewById(R.id.discountView);
        final MLBusinessDividingLineView dividingView = findViewById(R.id.dividingLineView);

        if (discountData != null) {
            discountView.init(discountData, onClickDiscountBox);
        } else {
            discountView.setVisibility(GONE);
            dividingView.setVisibility(GONE);
        }
    }

    private void renderDownload(@Nullable final MLBusinessDownloadAppData downloadAppData,
        @NonNull final Listener listener) {
        final MLBusinessDownloadAppView downloadAppView = findViewById(R.id.downloadView);
        if (downloadAppData != null && !isMPInstalled(getContext().getPackageManager())) {
            downloadAppView.init(downloadAppData, listener);
        } else {
            downloadAppView.setVisibility(GONE);
        }
    }

    private void renderCrossSellingBox(
        @NonNull final List<MLBusinessCrossSellingBoxData> crossSellingBoxDataList,
        @NonNull final Listener listener) {

        final LinearLayout businessComponents = findViewById(R.id.businessComponents);

        for (final MLBusinessCrossSellingBoxData crossSellingData : crossSellingBoxDataList) {
            final MLBusinessCrossSellingBoxView crossSellingBoxView =
                new MLBusinessCrossSellingBoxView(getContext());
            crossSellingBoxView.init(crossSellingData, listener);
            businessComponents.addView(crossSellingBoxView);
        }
    }

    private void renderReceipt(@Nullable final String receiptId) {
        final PaymentResultReceipt receipt = findViewById(R.id.receipt);
        if (TextUtil.isNotEmpty(receiptId)) {
            receipt.setVisibility(VISIBLE);
            receipt.setReceiptId(receiptId);
        } else {
            receipt.setVisibility(GONE);
        }
    }

    private void renderHelp(@Nullable final String help) {
        final View helpContainer = findViewById(R.id.help);
        if (TextUtil.isNotEmpty(help)) {
            helpContainer.setVisibility(VISIBLE);
            final TextView helpTitle = helpContainer.findViewById(R.id.help_title);
            final TextView helpDescription = helpContainer.findViewById(R.id.help_description);
            helpTitle.setText(R.string.px_what_can_do);
            helpDescription.setText(help);
        } else {
            helpContainer.setVisibility(GONE);
        }
    }

    private void renderMethods(@NonNull final Model model) {
        final PaymentResultMethod primaryMethod = findViewById(R.id.primaryMethod);
        final PaymentResultMethod secondaryMethod = findViewById(R.id.secondaryMethod);

        primaryMethod.setVisibility(GONE);
        secondaryMethod.setVisibility(GONE);

        if (model.methodModels != null && !model.methodModels.isEmpty()) {
            if (model.methodModels.size() > 1) {
                secondaryMethod.setVisibility(VISIBLE);
                secondaryMethod.setModel(model.methodModels.get(1));
            }
            primaryMethod.setVisibility(VISIBLE);
            primaryMethod.setModel(model.methodModels.get(0));
        }
    }

    private void renderViewReceipt(@Nullable final Action viewReceiptAction, final Listener listener) {
        final MeliButton viewReceiptButton = findViewById(R.id.view_receipt_action);
        final String target = viewReceiptAction != null ? viewReceiptAction.getTarget() : null;
        if (TextUtil.isNotEmpty(target) && isMPInstalled(getContext().getPackageManager())) {
            viewReceiptButton
                .setBackground(ContextCompat.getDrawable(getContext(), R.drawable.px_quiet_button_selector));
            viewReceiptButton.setText(viewReceiptAction.getLabel());
            viewReceiptButton.setOnClickListener(
                v -> listener.onClickViewReceipt(target));
        } else {
            viewReceiptButton.setVisibility(GONE);
        }
    }

    private void renderFragment(@IdRes final int id, @Nullable final ExternalFragment fragment) {
        final ViewGroup container = findViewById(id);
        if (fragment != null) {
            container.setVisibility(VISIBLE);
            FragmentUtil.replaceFragment(container, fragment);
        } else {
            container.setVisibility(GONE);
        }
    }

    public static final class Model {
        /* default */ final List<PaymentResultMethod.Model> methodModels;
        /* default */ final CongratsViewModel congratsViewModel;
        @Nullable /* default */ final String receiptId;
        @Nullable /* default */ final String help;
        @Nullable /* default */ final String statement;
        @Nullable /* default */ final ExternalFragment topFragment;
        @Nullable /* default */ final ExternalFragment bottomFragment;
        @Nullable /* default */ final ExternalFragment importantFragment;

        public Model(@NonNull final Builder builder) {
            methodModels = builder.methodModels;
            congratsViewModel = builder.congratsViewModel;
            receiptId = builder.receiptId;
            help = builder.help;
            statement = builder.statement;
            topFragment = builder.topFragment;
            bottomFragment = builder.bottomFragment;
            importantFragment = builder.importantFragment;
        }

        public static class Builder {
            /* default */ List<PaymentResultMethod.Model> methodModels;
            /* default */ CongratsViewModel congratsViewModel;
            @Nullable /* default */ String receiptId;
            @Nullable /* default */ String help;
            @Nullable /* default */ String statement;
            @Nullable /* default */ ExternalFragment topFragment;
            @Nullable /* default */ ExternalFragment bottomFragment;
            @Nullable /* default */ ExternalFragment importantFragment;

            public Builder setMethodModels(@NonNull final List<PaymentResultMethod.Model> methodModels) {
                this.methodModels = methodModels;
                return this;
            }

            public Builder setCongratsViewModel(
                @NonNull final CongratsViewModel rewardResultViewModel) {
                this.congratsViewModel = rewardResultViewModel;
                return this;
            }

            public Builder setReceiptId(@Nullable final String receiptId) {
                this.receiptId = receiptId;
                return this;
            }

            public Builder setHelp(@Nullable final String help) {
                this.help = help;
                return this;
            }

            public Builder setTopFragment(@Nullable final ExternalFragment topFragment) {
                this.topFragment = topFragment;
                return this;
            }

            public Builder setBottomFragment(@Nullable final ExternalFragment bottomFragment) {
                this.bottomFragment = bottomFragment;
                return this;
            }

            public Builder setImportantFragment(@Nullable final ExternalFragment importantFragment) {
                this.importantFragment = importantFragment;
                return this;
            }

            public Builder setStatement(@Nullable final String statement) {
                this.statement = statement;
                return this;
            }

            public Model build() {
                return new Model(this);
            }
        }
    }
}
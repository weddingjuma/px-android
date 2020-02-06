package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.addons.model.SecurityValidationData;
import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.internal.callbacks.PaymentServiceHandler;
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator;
import com.mercadopago.android.px.internal.viewmodel.AmountLocalized;
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

public interface OfflineMethods {

    interface OffMethodsView extends MvpView {
        void startLoadingButton(final int paymentTimeout, @NonNull final PayButtonViewModel payButtonViewModel);

        void showPaymentResult(IPaymentDescriptor payment);

        void finishLoading(@NonNull final ExplodeDecorator params);

        void cancelLoading();

        boolean isExploding();

        void showErrorSnackBar(MercadoPagoError error);

        void showErrorScreen(MercadoPagoError error);

        void showPaymentProcessor();

        void updateTotalView(@NonNull final AmountLocalized amountLocalized);

        void onSlideSheet(final float offset);

        void onSheetStateChanged(int newSheetState);

        void startSecurityValidation(SecurityValidationData data);

        void startKnowYourCustomerFlow(@NonNull final String flowLink);
    }

    interface Actions extends PaymentServiceHandler {

        void onViewResumed();

        void onViewPaused();

        void updateModel();

        void selectMethod(@NonNull final OfflineMethodItem selectedItem);

        void startSecuredPayment();

        void startPayment();

        void trackSecurityFriction();

        void trackAbort();
    }
}
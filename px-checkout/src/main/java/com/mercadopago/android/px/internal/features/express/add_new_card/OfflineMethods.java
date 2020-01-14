package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
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

        void disableCloseButton();

        void showPaymentResult(IPaymentDescriptor payment);

        void finishLoading(@NonNull final ExplodeDecorator params);

        void cancelLoading();

        void showErrorSnackBar(MercadoPagoError error);

        void showErrorScreen(MercadoPagoError error);

        void showPaymentProcessor();

        void updateTotalView(@NonNull final AmountLocalized amountLocalized);
    }

    interface Actions extends PaymentServiceHandler {

        void onViewResumed();

        void onViewPaused();

        void loadViewModel();

        void selectMethod(@NonNull final OfflineMethodItem selectedItem);
    }
}
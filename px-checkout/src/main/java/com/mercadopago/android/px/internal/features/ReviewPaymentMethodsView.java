package com.mercadopago.android.px.internal.features;

import com.mercadopago.android.px.internal.base.MvpView;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import java.util.List;

public interface ReviewPaymentMethodsView extends MvpView {

    void showError(MercadoPagoError error, String requestOrigin);

    void initializeSupportedPaymentMethods(List<PaymentMethod> supportedPaymentMethods);
}

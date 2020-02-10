package com.mercadopago.android.px.internal.features.explode;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.features.PaymentResultViewModelFactory;
import com.mercadopago.android.px.internal.features.payment_result.PaymentResultDecorator;
import com.mercadopago.android.px.internal.viewmodel.PaymentResultType;
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper;
import com.mercadopago.android.px.model.BusinessPayment;
import com.mercadopago.android.px.model.IPaymentDescriptor;

public class ExplodeDecoratorMapper extends Mapper<IPaymentDescriptor, ExplodeDecorator> {

    @Override
    public ExplodeDecorator map(@NonNull final IPaymentDescriptor val) {
        if (val instanceof BusinessPayment) {
            return ExplodeDecorator.from(PaymentResultType.from(((BusinessPayment) val).getDecorator()));
        }
        final PaymentResultDecorator decorator = PaymentResultViewModelFactory.createPaymentResultDecorator(val);
        return new ExplodeDecorator(decorator.getPrimaryColor(), decorator.getStatusIcon());
    }
}
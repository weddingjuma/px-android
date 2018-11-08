package com.mercadopago.android.px.internal.features.express.components;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import com.mercadolibre.android.ui.widgets.MeliDialog;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.di.Session;

public class PaymentDetailInfoDialog extends MeliDialog {

    private static final String TAG = PaymentDetailInfoDialog.class.getName();

    public static void showDialog(@NonNull final FragmentManager fragmentManager) {
        final PaymentDetailInfoDialog paymentDetailInfoDialog = new PaymentDetailInfoDialog();
        final Bundle bundle = new Bundle();
        paymentDetailInfoDialog.setArguments(bundle);
        paymentDetailInfoDialog.show(fragmentManager, TAG);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Session session = Session.getSession(view.getContext());
        final PaymentDetailContainer container =
            new PaymentDetailContainer(new PaymentDetailContainer.Props(session.getDiscountRepository(),
                session.getConfigurationModule().getPaymentSettings().getCheckoutPreference().getItems(),
                session.getConfigurationModule().getPaymentSettings().getCheckoutPreference().getSite()
                    .getCurrencyId()));
        container.render((ViewGroup) view.findViewById(R.id.main_container));
    }

    @Override
    public int getContentView() {
        return R.layout.px_fragment_dialog_payment_detail_info;
    }

    @Nullable
    @Override
    public String getTitle() {
        return getContext().getString(R.string.px_payment_detail_title_modal);
    }
}

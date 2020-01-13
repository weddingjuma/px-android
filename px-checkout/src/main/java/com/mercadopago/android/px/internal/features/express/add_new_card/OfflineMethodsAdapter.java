package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.OfflinePaymentType;
import java.util.List;

class OfflineMethodsAdapter extends RecyclerView.Adapter<OfflineMethodsRowHolder> {

    private List<OfflinePaymentType> paymentTypes;

    public OfflineMethodsAdapter() {
        this.paymentTypes = paymentTypes;
    }

    @NonNull
    @Override
    public OfflineMethodsRowHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int i) {
        final View offlineMethodView = LayoutInflater.from(parent.getContext()).inflate(R.layout.px_view_offline_payment_method_item, parent, false);
        return new OfflineMethodsRowHolder(offlineMethodView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfflineMethodsRowHolder offlineMethodsRowHolder, final int i) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

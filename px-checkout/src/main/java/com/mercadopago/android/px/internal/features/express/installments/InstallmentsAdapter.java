package com.mercadopago.android.px.internal.features.express.installments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.model.Currency;
import com.mercadopago.android.px.model.PayerCost;
import java.util.List;

import static com.mercadopago.android.px.internal.util.TextUtil.isEmpty;
import static com.mercadopago.android.px.model.PayerCost.NO_SELECTED;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentRowHolder> {

    @NonNull private final Currency currency;
    @NonNull private List<PayerCost> payerCosts;
    private int payerCostSelected;
    @NonNull private final ItemListener itemListener;
    private boolean hasReimbursement;

    public interface ItemListener {
        void onClick(final PayerCost payerCostSelected);
    }

    public InstallmentsAdapter(@NonNull final Currency currency, @NonNull final List<PayerCost> payerCosts,
        @NonNull final ItemListener itemListener) {
        this.currency = currency;
        this.payerCosts = payerCosts;
        this.itemListener = itemListener;
        payerCostSelected = NO_SELECTED;
        hasReimbursement = hasReimbursement(payerCosts);
    }

    public void setPayerCosts(@NonNull final List<PayerCost> payerCosts) {
        this.payerCosts = payerCosts;
        hasReimbursement = hasReimbursement(payerCosts);
    }

    public void setPayerCostSelected(final int payerCostSelected) {
        this.payerCostSelected = payerCostSelected;
    }

    private boolean hasReimbursement(@NonNull final Iterable<PayerCost> payerCosts) {
        boolean hasReimbursement = false;
        for (final PayerCost payerCost : payerCosts) {
            hasReimbursement |=
                payerCost.getReimbursement() != null && !isEmpty(payerCost.getReimbursement().getMessage());
        }
        return hasReimbursement;
    }

    @NonNull
    @Override
    public InstallmentRowHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View installmentView =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.px_view_payer_cost_item, parent, false);
        return new InstallmentRowHolder(installmentView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InstallmentRowHolder holder, final int position) {
        holder.populate(itemListener, currency, payerCosts.get(position), hasReimbursement);
        if (position == payerCostSelected) {
            holder.highLight();
        } else {
            holder.noHighLight();
        }
    }

    @Override
    public int getItemCount() {
        return payerCosts.size();
    }
}
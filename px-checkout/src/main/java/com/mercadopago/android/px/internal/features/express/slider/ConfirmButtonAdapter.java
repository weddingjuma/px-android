package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import com.mercadolibre.android.ui.widgets.MeliButton;
import com.mercadopago.android.px.internal.viewmodel.ConfirmButtonViewModel;
import com.mercadopago.android.px.internal.viewmodel.SplitSelectionState;
import java.util.List;

public class ConfirmButtonAdapter extends HubableAdapter<List<ConfirmButtonViewModel>, MeliButton> {

    public ConfirmButtonAdapter(@NonNull final MeliButton view) {
        super(view);
    }

    @Override
    public void updateData(final int currentIndex, final int payerCostSelected,
        @NonNull final SplitSelectionState splitSelectionState) {
        if (data.get(currentIndex).isDisabled()) {
            view.setState(MeliButton.State.DISABLED);
        } else {
            view.setState(MeliButton.State.NORMAL);
        }
    }

    @Override
    public List<ConfirmButtonViewModel> getNewModels(final HubAdapter.Model model) {
        return model.confirmButtonViewModels;
    }
}
package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.base.BaseFragment;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;

public class OfflineMethodsFragment extends BaseFragment<OfflineMethodsPresenter, OfflinePaymentTypesMetadata> {

    private RecyclerView methodsRecyclerView;
    private OfflineMethodsAdapter offlineMethodsAdapter;

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
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        methodsRecyclerView = view.findViewById(R.id.methods);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        methodsRecyclerView.setLayoutManager(linearLayoutManager);
        methodsRecyclerView
            .addItemDecoration(new DividerItemDecoration(view.getContext(), linearLayoutManager.getOrientation()));

        offlineMethodsAdapter = new OfflineMethodsAdapter();
        methodsRecyclerView.setAdapter(offlineMethodsAdapter);
    }

    @Override
    protected OfflineMethodsPresenter createPresenter() {
        return new OfflineMethodsPresenter();
    }
}


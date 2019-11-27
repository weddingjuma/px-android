package com.mercadopago.android.px.internal.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseFragment<P extends BasePresenter, M extends Parcelable> extends Fragment implements MvpView {

    private static final String ARG_MODEL = "ARG_MODEL";

    protected P presenter;
    protected M model;

    protected abstract P createPresenter();

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(final Context context) {
        super.onAttach(context);
        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_MODEL)) {
            //noinspection ConstantConditions
            model = getArguments().getParcelable(ARG_MODEL);
        } else {
            throw new IllegalStateException(getClass().getSimpleName() + " does not contain model info");
        }
        presenter = createPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDetach();
    }

    protected void storeModel(final M model) {
        final Bundle bundle = getArguments() != null ? getArguments() : new Bundle();
        bundle.putParcelable(ARG_MODEL, model);
        setArguments(bundle);
    }

}
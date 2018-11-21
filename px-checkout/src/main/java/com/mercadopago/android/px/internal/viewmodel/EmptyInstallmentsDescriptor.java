package com.mercadopago.android.px.internal.viewmodel;

import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;

public class EmptyInstallmentsDescriptor extends InstallmentsDescriptorView.Model {

    protected EmptyInstallmentsDescriptor() {
        super();
    }

    public static InstallmentsDescriptorView.Model create() {
        return new EmptyInstallmentsDescriptor();
    }
}

package com.mercadopago.android.px.addons;

import android.support.annotation.NonNull;

public final class PXBehaviourConfigurer {

    private SecurityBehaviour securityBehaviour;
    private ESCManagerBehaviour escManagerBehaviour;
    private TrackingBehaviour trackingBehaviour;

    public PXBehaviourConfigurer with(@NonNull final ESCManagerBehaviour escManagerBehaviour) {
        this.escManagerBehaviour = escManagerBehaviour;
        return this;
    }

    public PXBehaviourConfigurer with(@NonNull final SecurityBehaviour securityBehaviour) {
        this.securityBehaviour = securityBehaviour;
        return this;
    }

    public PXBehaviourConfigurer with(@NonNull final TrackingBehaviour trackingBehaviour) {
        this.trackingBehaviour = trackingBehaviour;
        return this;
    }

    public void configure() {
        BehaviourProvider.set(securityBehaviour);
        BehaviourProvider.set(escManagerBehaviour);
        BehaviourProvider.set(trackingBehaviour);
    }
}
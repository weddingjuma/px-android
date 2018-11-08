package com.mercadopago.android.px.testcheckout.idleresources;

import android.support.test.espresso.IdlingRegistry;
import com.mercadopago.android.px.internal.features.paymentresult.PaymentResultActivity;
import com.mercadopago.android.testlib.idlingresource.ActivityIdlingResource;

public class WaitForCongrats {

    private final IdlingRegistry register;
    private final ActivityIdlingResource activityIdlingResource;

    public WaitForCongrats() {
        register = IdlingRegistry.getInstance();
        activityIdlingResource = new ActivityIdlingResource(PaymentResultActivity.class);
    }

    public void start() {
        //Here wait for activity while animation exploding button starts.
        register.register(activityIdlingResource);
    }

    public void stop() {
        register.unregister(activityIdlingResource);
    }
}

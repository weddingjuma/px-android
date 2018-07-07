package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.android.px.testcheckout.flows.OffPaymentTypeTestFlow;
import com.mercadopago.android.px.testcheckout.pages.CongratsPage;
import com.mercadopago.testlib.HttpResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OfflinePaymentMethodTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
            new ActivityTestRule<>(CheckoutExampleActivity.class);


    private OffPaymentTypeTestFlow offPaymentTypeTestFlow;

    @Before
    public void setUp() {
        MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout.Builder("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03", "243966003-faedce8f-ee83-40a7-b8e6-bba34928383d");
        offPaymentTypeTestFlow = new OffPaymentTypeTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void withOffPagoFacilFlowOk() {
        CongratsPage congratsPage = offPaymentTypeTestFlow.runOffPaymentTypeFlow("Pago Fácil");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffRapipagoFlowOk() {
        CongratsPage congratsPage = offPaymentTypeTestFlow.runOffPaymentTypeFlow("Rapipago");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffProvinciaNETFlowOk() {
        CongratsPage congratsPage = offPaymentTypeTestFlow.runOffPaymentTypeFlow("Provincia NET");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffKioscosYComerciosCercanosFlowOk() {
        CongratsPage congratsPage = offPaymentTypeTestFlow.runOffPaymentTypeFlow("Kioscos y comercios cercanos");
        assertNotNull(congratsPage);
    }
}
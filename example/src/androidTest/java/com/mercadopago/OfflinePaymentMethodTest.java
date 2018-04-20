package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.testcheckout.CheckoutResource;
import com.mercadopago.testcheckout.flows.CheckoutTestFlow;
import com.mercadopago.testcheckout.pages.CongratsPage;
import com.mercadopago.testlib.HttpResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OfflinePaymentMethodTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
            new ActivityTestRule<>(CheckoutExampleActivity.class);


    private CheckoutTestFlow checkoutTestFlow;

    @Before
    public void setUp() {
        MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout.Builder();
        builder.setCheckoutPreference(new CheckoutPreference("243966003-faedce8f-ee83-40a7-b8e6-bba34928383d"));
        builder.setPublicKey("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03");
        builder.setActivity(activityRule.getActivity());
        checkoutTestFlow = CheckoutTestFlow.createFlowWithCheckout(builder);
    }

    @Test
    public void withOffPagoFacilFlowOk() {
        CongratsPage congratsPage = checkoutTestFlow.runOff("Pago Fácil");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffRapipagoFlowOk() {
        CongratsPage congratsPage = checkoutTestFlow.runOff("Rapipago");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffProvinciaNETFlowOk() {
        CongratsPage congratsPage = checkoutTestFlow.runOff("Provincia NET");
        assertNotNull(congratsPage);
    }

    @Test
    public void withOffKioscosYComerciosCercanosFlowOk() {
        CongratsPage congratsPage = checkoutTestFlow.runOff("Kioscos y comercios cercanos");
        assertNotNull(congratsPage);
    }
}

package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.testcheckout.CheckoutResource;
import com.mercadopago.testcheckout.flows.CheckoutTestFlow;
import com.mercadopago.testcheckout.input.Card;
import com.mercadopago.testcheckout.input.FakeCard;
import com.mercadopago.testcheckout.pages.CongratsPage;
import com.mercadopago.testlib.HttpResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DefaultInstallmentsTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
            new ActivityTestRule<>(CheckoutExampleActivity.class);

    private CheckoutTestFlow checkoutTestFlow;

    @Before
    public void setUp() {
        MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout.Builder();
        builder.setCheckoutPreference(new CheckoutPreference("243966003-0e1df452-28e3-4d72-8b69-a71123b8a626"));
        builder.setPublicKey("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03");
        builder.setActivity(activityRule.getActivity());
        checkoutTestFlow = CheckoutTestFlow.createFlowWithCheckout(builder);
    }

    @Test
    public void whenInstallmentsDefaultMaster() {
        Card card = new FakeCard(FakeCard.CardState.APRO, "5323793735506106");
        CongratsPage congratsPage = checkoutTestFlow.runCreditCardPaymentFlowNoInstallmentsOptionAndBankSelection(card, 4);
        assertNotNull(congratsPage);
    }
}

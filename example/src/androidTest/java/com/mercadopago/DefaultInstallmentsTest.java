package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.testcheckout.flows.CreditCardTestFlow;
import com.mercadopago.testcheckout.input.Card;
import com.mercadopago.testcheckout.input.FakeCard;
import com.mercadopago.testcheckout.pages.CongratsPage;
import com.mercadopago.testlib.HttpResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DefaultInstallmentsTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
            new ActivityTestRule<>(CheckoutExampleActivity.class);

    private CreditCardTestFlow creditCardTestFlow;

    @Before
    public void setUp() {
        MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout.Builder("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03",
                "243966003-0e1df452-28e3-4d72-8b69-a71123b8a626");
        creditCardTestFlow = new CreditCardTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void whenInstallmentsDefaultMaster() {
        Card card = new FakeCard(FakeCard.CardState.APRO, "5323793735506106");
        CongratsPage congratsPage = creditCardTestFlow.runCreditCardPaymentFlowNoInstallmentsOptionAndBankSelection(card, 4);
        assertNotNull(congratsPage);
    }
}

package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.testcheckout.flows.CreditCardTestFlow;
import com.mercadopago.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.testcheckout.input.Card;
import com.mercadopago.testcheckout.input.Country;
import com.mercadopago.testcheckout.input.FakeCard;
import com.mercadopago.testcheckout.input.Master;
import com.mercadopago.testcheckout.input.Visa;
import com.mercadopago.testcheckout.pages.CongratsPage;
import com.mercadopago.testcheckout.pages.ReviewPaymentMethodsPage;
import com.mercadopago.testlib.HttpResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExclusionPaymentMethodTest {

    private CreditCardTestFlow creditCardTestFlow;

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
        new ActivityTestRule<>(CheckoutExampleActivity.class);

    @Before
    public void setUp() {
        final MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout
            .Builder("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03",
            "243966003-55f883b7-2cfb-4266-8001-11e081a45797");
        creditCardTestFlow = new CreditCardTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void whenVisaExcludedThenCompleteFlowWithMaster() {
        final Card card = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);
        final CongratsPage congratsPage = creditCardTestFlow.runCreditCardPaymentFlowWithInstallments(card, 1);
        assertNotNull(congratsPage);
    }

    @Test
    public void whenVisaExcludedThenShowAvailablePaymentMethodsWithVisa() {
        final Card card = new Visa(FakeCard.CardState.APRO, Country.ARGENTINA);
        final ReviewPaymentMethodsPage reviewPaymentMethodsPage =
            creditCardTestFlow.runCreditCardPaymentFlowWithPaymentMethodExcluded(card);
        assertNotNull(reviewPaymentMethodsPage);
    }

    @Test
    public void whenVisaExcludedThenShowAvailablePaymentMethodsWithVisaAndEnterSupportedCardMaster() {
        final Card excludedCard = new Visa(FakeCard.CardState.APRO, Country.ARGENTINA);
        final Card supportedCard = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);
        final CongratsPage congratsPage = creditCardTestFlow
            .runCreditCardPaymentFlowWithPaymentMethodExcludedAndTryAnotherCard(excludedCard, supportedCard);
        assertNotNull(congratsPage);
    }
}

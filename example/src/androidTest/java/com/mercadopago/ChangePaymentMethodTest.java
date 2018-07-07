package com.mercadopago;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.testcheckout.flows.CreditCardTestFlow;
import com.mercadopago.android.px.testcheckout.flows.DebitCardTestFlow;
import com.mercadopago.android.px.testcheckout.flows.OffPaymentTypeTestFlow;
import com.mercadopago.android.px.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.android.px.testcheckout.input.Card;
import com.mercadopago.android.px.testcheckout.input.Country;
import com.mercadopago.android.px.testcheckout.input.FakeCard;
import com.mercadopago.android.px.testcheckout.input.Master;
import com.mercadopago.android.px.testcheckout.input.Visa;
import com.mercadopago.android.px.testcheckout.input.VisaDebit;
import com.mercadopago.android.px.testcheckout.pages.CongratsPage;
import com.mercadopago.android.px.testcheckout.pages.PaymentMethodPage;
import com.mercadopago.android.px.testcheckout.pages.ReviewAndConfirmPage;
import com.mercadopago.testlib.HttpResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangePaymentMethodTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
        new ActivityTestRule<>(CheckoutExampleActivity.class);

    private CreditCardTestFlow creditCardTestFlow;
    private DebitCardTestFlow debitCardTestFlow;
    private OffPaymentTypeTestFlow offPaymentTypeTestFlow;

    @Before
    public void setUp() {
        final MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout
            .Builder("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03",
            "243966003-d0be0be0-6fd8-4769-bf2f-7f2d979655f5");
        creditCardTestFlow = new CreditCardTestFlow(builder.build(), activityRule.getActivity());
        debitCardTestFlow = new DebitCardTestFlow(builder.build(), activityRule.getActivity());
        offPaymentTypeTestFlow = new OffPaymentTypeTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void changeCreditCardFromMasterToVisa() {
        final Card firstCard = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);
        final Card secondCard = new Visa(FakeCard.CardState.APRO, Country.ARGENTINA);

        final ReviewAndConfirmPage reviewAndConfirmPage = creditCardTestFlow
            .runCreditCardUntilReviewAndConfirm(firstCard);
        final PaymentMethodPage paymentMethodPage = reviewAndConfirmPage.clickChangePaymentMethod();
        final CongratsPage congratsPage = creditCardTestFlow
            .runCreditCardFlowFromPaymentMethod(secondCard, paymentMethodPage);
        assertNotNull(congratsPage);
    }

    @Test
    public void changeCreditCardToDebitCard() {
        final Card firstCard = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);
        final Card secondCard = new VisaDebit(FakeCard.CardState.APRO, Country.ARGENTINA);
        final ReviewAndConfirmPage reviewAndConfirmPage = creditCardTestFlow
            .runCreditCardUntilReviewAndConfirm(firstCard);
        final PaymentMethodPage paymentMethodPage = reviewAndConfirmPage.clickChangePaymentMethod();
        final CongratsPage congratsPage = debitCardTestFlow
            .runDebitCardFlowFromPaymentMethod(secondCard, paymentMethodPage);
        assertNotNull(congratsPage);
    }

    @Test
    public void changeCreditCardToPaymentOff() {
        final Card card = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);

        final ReviewAndConfirmPage reviewAndConfirmPage = creditCardTestFlow
            .runCreditCardUntilReviewAndConfirm(card);
        final PaymentMethodPage paymentMethodPage = reviewAndConfirmPage.clickChangePaymentMethod();
        final CongratsPage congratsPage = offPaymentTypeTestFlow
            .runOffPaymentMethodFromPaymentMethodFlow("Pago Fácil", paymentMethodPage);
        assertNotNull(congratsPage);
    }

    @Test
    public void changePaymentOffToDebitCard() {
        final Card card = new VisaDebit(FakeCard.CardState.APRO, Country.ARGENTINA);

        final ReviewAndConfirmPage reviewAndConfirmPage = offPaymentTypeTestFlow
            .runOffPaymentTypeFlowUntilReviewAndConfirm("Pago Fácil");
        final PaymentMethodPage paymentMethodPage = reviewAndConfirmPage.clickChangePaymentMethod();
        final CongratsPage congratsPage = debitCardTestFlow
            .runDebitCardFlowFromPaymentMethod(card, paymentMethodPage);
        assertNotNull(congratsPage);
    }

}

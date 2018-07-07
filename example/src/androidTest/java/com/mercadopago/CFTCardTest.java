package com.mercadopago;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.testcheckout.assertions.DefaultValidator;
import com.mercadopago.testcheckout.flows.CreditCardTestFlow;
import com.mercadopago.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.testcheckout.input.Card;
import com.mercadopago.testcheckout.input.Country;
import com.mercadopago.testcheckout.input.FakeCard;
import com.mercadopago.testcheckout.input.Master;
import com.mercadopago.testcheckout.input.NoInterestMaster;
import com.mercadopago.testcheckout.pages.CongratsPage;
import com.mercadopago.testcheckout.pages.ReviewAndConfirmPage;
import com.mercadopago.testlib.HttpResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CFTCardTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
        new ActivityTestRule<>(CheckoutExampleActivity.class);

    private CreditCardTestFlow creditCardTestFlow;

    @Before
    public void setUp() {
        final MercadoPagoCheckout.Builder builder = new MercadoPagoCheckout
            .Builder("APP_USR-0d933ff3-b803-4999-a211-8b3c7d5c7c03",
            "243966003-d0be0be0-6fd8-4769-bf2f-7f2d979655f5");
        creditCardTestFlow = new CreditCardTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void whenMasterWithWithInterestShowCFT() {
        final Card card = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);

        final CongratsPage congratsPage = creditCardTestFlow.runCreditCardPaymentFlowWithInstallments(card, 1,
            new DefaultValidator() {
                @Override
                public void validate(@NonNull final ReviewAndConfirmPage reviewAndConfirmPage) {
                    super.validate(reviewAndConfirmPage);
                    onView(ViewMatchers.withId(R.id.disclaimer))
                        .check(matches(withEffectiveVisibility(
                            ViewMatchers.Visibility.VISIBLE)));
                    onView(ViewMatchers.withId(R.id.cftText))
                        .check(matches(withText("CFT: 120,42%")));
                }
            });
        assertNotNull(congratsPage);
    }

    @Test
    public void whenMasterWithNoInterestShowCFT() {
        final Card card = new NoInterestMaster(FakeCard.CardState.APRO, Country.ARGENTINA);

        CongratsPage congratsPage =
            creditCardTestFlow.runCreditCardPaymentFlowWithBankSelectionAndInstallmentsOption(card, 2,
                1, new DefaultValidator() {
                    @Override
                    public void validate(@NonNull final ReviewAndConfirmPage reviewAndConfirmPage) {
                        super.validate(reviewAndConfirmPage);
                        onView(ViewMatchers.withId(R.id.disclaimer))
                            .check(matches(withEffectiveVisibility(
                                ViewMatchers.Visibility.VISIBLE)));
                        onView(ViewMatchers.withId(R.id.cftText))
                            .check(matches(withText("CFT: 0,00%")));
                    }
                });
        assertNotNull(congratsPage);
    }


}

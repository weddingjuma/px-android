package com.mercadopago;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.testcheckout.assertions.DefaultValidator;
import com.mercadopago.android.px.testcheckout.flows.CreditCardTestFlow;
import com.mercadopago.android.px.testcheckout.idleresources.CheckoutResource;
import com.mercadopago.android.px.testcheckout.input.Card;
import com.mercadopago.android.px.testcheckout.input.Country;
import com.mercadopago.android.px.testcheckout.input.FakeCard;
import com.mercadopago.android.px.testcheckout.input.Master;
import com.mercadopago.android.px.testcheckout.input.Visa;
import com.mercadopago.android.px.testcheckout.pages.CreditCardPage;
import com.mercadopago.android.px.testcheckout.pages.NamePage;
import com.mercadopago.example.R;
import com.mercadopago.android.testlib.HttpResource;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UniquePaymentMethodTest {

    @Rule
    public HttpResource httpResource = new CheckoutResource();

    @Rule
    public ActivityTestRule<CheckoutExampleActivity> activityRule =
        new ActivityTestRule<>(CheckoutExampleActivity.class);

    private CreditCardTestFlow creditCardTestFlow;

    @Before
    public void setUp() {
        MercadoPagoCheckout.Builder builder =
            new MercadoPagoCheckout.Builder("APP_USR-2681ea61-10af-4bf6-a73d-e426d6b07e2c",
                "243962506-76f3ae80-28de-4c8a-94a5-dad78ef8b4c4");
        creditCardTestFlow = new CreditCardTestFlow(builder.build(), activityRule.getActivity());
    }

    @Test
    public void whenVisaIsUniquePaymentMethodThenShowMessage() {
        final Card card = new Master(FakeCard.CardState.APRO, Country.ARGENTINA);
        final CreditCardPage creditCardPage = creditCardTestFlow.runCreditCardPaymentFlowWithUniquePaymentMethod(card,
            new DefaultValidator() {
                @Override
                public void validate(@NonNull final CreditCardPage creditCardPage) {
                    super.validate(creditCardPage);
                    final Matcher<View> messageContainerMatcher = withId(R.id.mpsdkBlackInfoContainer);
                    onView(messageContainerMatcher)
                        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
                }
            });
        assertNotNull(creditCardPage);
    }

    @Test
    public void whenVisaIsUniquePaymentMethodThenShowMessageAndOnEnterCardHideMessage() {
        final Card card = new Visa(FakeCard.CardState.APRO, Country.ARGENTINA);
        final NamePage namePage = creditCardTestFlow.runCreditCardPaymentFlowWithUniquePaymentMethodAndEnterNumber(card,
            new DefaultValidator() {
                @Override
                public void validate(@NonNull final NamePage namePage) {
                    super.validate(namePage);
                    final Matcher<View> messageContainerMatcher = withId(R.id.mpsdkBlackInfoContainer);
                    onView(messageContainerMatcher)
                        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
                }
            });
        assertNotNull(namePage);
    }
}

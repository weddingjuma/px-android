package com.mercadopago.android.px.model;

import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.internal.features.CheckoutActivity;
import com.mercadopago.android.px.test.BaseTest;
import com.mercadopago.android.px.test.StaticMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CardTest extends BaseTest<CheckoutActivity> {

    public CardTest() {
        setup(CheckoutActivity.class);
    }

    @Test
    public void testIsSecurityCodeRequired() {
        final Card card = StaticMock.getCard();
        assertTrue(card.isSecurityCodeRequired());
    }

    @Test
    public void testIsSecurityCodeRequiredNull() {
        final Card card = StaticMock.getCard();
        card.setSecurityCode(null);
        assertFalse(card.isSecurityCodeRequired());
    }

    @Test
    public void testIsSecurityCodeRequiredLengthZero() {
        final Card card = StaticMock.getCard();
        card.getSecurityCode().setLength(0);
        assertFalse(card.isSecurityCodeRequired());
    }
}
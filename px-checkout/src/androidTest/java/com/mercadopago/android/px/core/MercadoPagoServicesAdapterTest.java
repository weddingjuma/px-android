package com.mercadopago.android.px.core;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.internal.datasource.MercadoPagoServicesAdapter;
import com.mercadopago.android.px.internal.features.checkout.CheckoutActivity;
import com.mercadopago.android.px.test.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MercadoPagoServicesAdapterTest extends BaseTest<CheckoutActivity> {

    public MercadoPagoServicesAdapterTest() {
        setup(CheckoutActivity.class);
    }

    @Test
    public void testWhenAndroidVersionIsPriorICSThenPropertyKeepAliveFalseElseDefault() {
        new MercadoPagoServicesAdapter(InstrumentationRegistry.getContext(), "DUMMY_PK");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            assertEquals("false", System.getProperty("http.keepAlive"));
        } else {
            assertNull(System.getProperty("http.keepAlive"));
        }
    }
}
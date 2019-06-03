package com.mercadopago.android.px.model;

import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import com.mercadopago.android.px.internal.features.CheckoutActivity;
import com.mercadopago.android.px.test.BaseTest;
import com.mercadopago.android.px.test.StaticMock;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AllModelTest extends BaseTest<CheckoutActivity> {

    public AllModelTest() {
        setup(CheckoutActivity.class);
    }

    @Test
    public void testAddress() {
        final Address address = new Address();
        address.setStreetName("abcd");
        address.setStreetNumber(Long.parseLong("100"));
        address.setZipCode("1000");
        assertEquals("abcd", address.getStreetName());
        assertEquals("100", Long.toString(address.getStreetNumber()));
        assertEquals("1000", address.getZipCode());
    }

    @Test
    public void testPayment() {
        final Payment payment = new Payment();
        payment.setBinaryMode(true);
        payment.setCallForAuthorizeId("123");
        payment.setCaptured(false);
        payment.setCard(StaticMock.getCard());
        payment.setCollectorId(1234567L);
        payment.setCouponAmount(new BigDecimal("19"));
        payment.setCurrencyId("ARS");
        payment.setDateApproved(getDummyDate("2015-01-01"));
        payment.setDateCreated(getDummyDate("2015-01-02"));
        payment.setDateLastUpdated(getDummyDate("2015-01-03"));
        payment.setDescription("some desc");
        payment.setDifferentialPricingId(Long.parseLong("789"));
        payment.setExternalReference("some ext ref");
        payment.setFeeDetails(StaticMock.getPayment(getApplicationContext()).getFeeDetails());
        payment.setId(Long.parseLong("123456"));
        payment.setInstallments(3);
        payment.setIssuerId("3");
        payment.setLiveMode(true);
        payment.setMetadata(null);
        payment.setMoneyReleaseDate(getDummyDate("2015-01-04"));
        payment.setNotificationUrl("http://some_url.com");
        payment.setOperationType(StaticMock.getPayment(getApplicationContext()).getOperationType());
        payment.setOrder(StaticMock.getPayment(getApplicationContext()).getOrder());
        payment.setPayer(StaticMock.getPayment(getApplicationContext()).getPayer());
        payment.setPaymentMethodId("visa");
        payment.setPaymentTypeId("credit_card");
        payment.setRefunds(null);
        payment.setStatementDescriptor("statement");
        payment.setStatus("approved");
        payment.setStatusDetail("accredited");
        payment.setTransactionAmount(new BigDecimal("10.50"));
        payment.setTransactionAmountRefunded(new BigDecimal("20.50"));
        payment.setTransactionDetails(StaticMock.getPayment(getApplicationContext()).getTransactionDetails());
        assertTrue(payment.getBinaryMode());
        assertEquals("123", payment.getCallForAuthorizeId());
        assertTrue(!payment.getCaptured());
        assertEquals("149024476", payment.getCard().getId());
        assertEquals(1234567L, (long) payment.getCollectorId());
        assertEquals("19", payment.getCouponAmount().toString());
        assertEquals("ARS", payment.getCurrencyId());
        assertTrue(validateDate(payment.getDateApproved(), "2015-01-01"));
        assertTrue(validateDate(payment.getDateCreated(), "2015-01-02"));
        assertTrue(validateDate(payment.getDateLastUpdated(), "2015-01-03"));
        assertEquals("some desc", payment.getDescription());
        assertEquals("789", Long.toString(payment.getDifferentialPricingId()));
        assertEquals("some ext ref", payment.getExternalReference());
        assertEquals("5.99", payment.getFeeDetails().get(0).getAmount().toString());
        assertEquals("123456", Long.toString(payment.getId()));
        assertEquals("3", Integer.toString(payment.getInstallments()));
        assertEquals("3", payment.getIssuerId());
        assertEquals(true, payment.getLiveMode());
        assertNull(payment.getMetadata());
        assertEquals(true, validateDate(payment.getMoneyReleaseDate(), "2015-01-04"));
        assertEquals("http://some_url.com", payment.getNotificationUrl());
        assertEquals("regular_payment", payment.getOperationType());
        assertNull(payment.getOrder().getId());
        assertEquals("178101336", payment.getPayer().getId());
        assertEquals("visa", payment.getPaymentMethodId());
        assertEquals("credit_card", payment.getPaymentTypeId());
        assertNull(payment.getRefunds());
        assertEquals("statement", payment.getStatementDescriptor());
        assertEquals("approved", payment.getPaymentStatus());
        assertEquals("accredited", payment.getPaymentStatusDetail());
        assertEquals("10.50", payment.getTransactionAmount().toString());
        assertEquals("20.50", payment.getTransactionAmountRefunded().toString());
        assertEquals("100", payment.getTransactionDetails().getTotalPaidAmount().toString());
    }

    @Nullable
    private Date getDummyDate(final String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (final Exception ex) {
            return null;
        }
    }

    @Nullable
    private Boolean validateDate(final Date date, final String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value).equals(date);
        } catch (final Exception ex) {
            return null;
        }
    }
}
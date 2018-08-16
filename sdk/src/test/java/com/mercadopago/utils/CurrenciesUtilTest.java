package com.mercadopago.utils;

import com.mercadopago.util.CurrenciesUtil;
import java.math.BigDecimal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrenciesUtilTest {

    @Test
    public void whenThirdDecimalBelowFiveThenRoundDownWithOneDigit() {
        final BigDecimal amount = new BigDecimal("5.432");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("5.43"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalAboveFiveThenRoundUpWithOneDigit() {
        final BigDecimal amount = new BigDecimal("5.436");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("5.44"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalEqualsFiveThenRoundUpWithOneDigit() {
        final BigDecimal amount = new BigDecimal("5.435");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("5.44"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalBelowFiveThenRoundDownWithTwoDigits() {
        final BigDecimal amount = new BigDecimal("25.432");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("25.43"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalAboveFiveThenRoundUpWithTwoDigits() {
        final BigDecimal amount = new BigDecimal("25.436");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("25.44"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalEqualsFiveThenRoundUpWithTwoDigits() {
        final BigDecimal amount = new BigDecimal("25.435");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("25.44"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalBelowFiveThenRoundDownWithThreeDigits() {
        final BigDecimal amount = new BigDecimal("425.432");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("425.43"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalAboveFiveThenRoundUpWithThreeDigits() {
        final BigDecimal amount = new BigDecimal("425.436");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("425.44"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalEqualsFiveThenRoundUpWithThreeDigits() {
        final BigDecimal amount = new BigDecimal("425.435");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("425.44"), roundedAmount);
    }

    @Test
    public void whenTwoDecimalsThenDontRound() {
        final BigDecimal amount = new BigDecimal("5.43");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("5.43"), roundedAmount);
    }

    @Test
    public void whenOneDecimalThenDontRound() {
        final BigDecimal amount = new BigDecimal("5.4");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("5.40"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalIsNineThenRoundUpWithThreeDigits() {
        final BigDecimal amount = new BigDecimal("425.099");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("425.10"), roundedAmount);
    }

    @Test
    public void whenThirdDecimalIsFiveThenRoundUpWithThreeDigits() {
        final BigDecimal amount = new BigDecimal("425.005");
        final BigDecimal roundedAmount = CurrenciesUtil.getRoundedAmount(amount);
        assertEquals(new BigDecimal("425.01"), roundedAmount);
    }
}

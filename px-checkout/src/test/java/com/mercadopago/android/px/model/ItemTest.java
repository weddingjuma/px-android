package com.mercadopago.android.px.model;

import com.mercadopago.android.px.internal.util.TextUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemTest {

    @Test
    public void testItemsValidationAllValid() {
        final Collection<Item> items = new ArrayList<>();
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ONE).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ONE).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.TEN.intValue(), BigDecimal.TEN).build());

        assertTrue(Item.areItemsValid(items));
    }

    @Test
    public void testItemsValidationAtLeastOneWithValue() {
        final Collection<Item> items = new ArrayList<>();
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ZERO).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ZERO).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.TEN.intValue(), BigDecimal.ONE).build());

        assertTrue(Item.areItemsValid(items));
    }

    @Test
    public void testItemsValidationAtLeastOneWithoutQuantity() {
        final Collection<Item> items = new ArrayList<>();
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ONE).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ONE).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ZERO.intValue(), BigDecimal.TEN).build());

        assertFalse(Item.areItemsValid(items));
    }

    @Test
    public void testItemsValidationAllWithoutValue() {
        final Collection<Item> items = new ArrayList<>();
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ZERO).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.ONE.intValue(), BigDecimal.ZERO).build());
        items.add(new Item.Builder(TextUtil.EMPTY, BigDecimal.TEN.intValue(), BigDecimal.ZERO).build());

        assertFalse(Item.areItemsValid(items));
    }
}
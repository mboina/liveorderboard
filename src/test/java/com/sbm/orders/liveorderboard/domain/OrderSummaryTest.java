package com.sbm.orders.liveorderboard.domain;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class OrderSummaryTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenMergingOrdersOfDifferentUnit() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(5.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary = new OrderSummary(orderQuantity,
                        unitPrice, OrderType.SELL);

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.POUND);
        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(305), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary summaryOrder2 = new OrderSummary(orderQuantity2,
                        unitPrice2, OrderType.SELL);

        // When
        orderSummary.merge(summaryOrder2);

        // Then fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenMergingOrdersOfDifferentUnitPrice() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(5.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary = new OrderSummary(orderQuantity,
                        unitPrice, OrderType.SELL);

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO);
        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(305), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary2 = new OrderSummary(orderQuantity2,
                        unitPrice2, OrderType.SELL);

        // When
        orderSummary.merge(orderSummary2);

        // Then fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenMergingOrdersOfDifferentType() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(5.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary = new OrderSummary(orderQuantity,
                        unitPrice, OrderType.BUY);

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO);
        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary2 = new OrderSummary(orderQuantity2,
                        unitPrice2, OrderType.SELL);

        // When
        orderSummary.merge(orderSummary2);

        // Then fail
    }

    @Test
    public void shouldMergeOrdersThatAreSimilarTypeWithSameUnitPrice() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(5.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary = new OrderSummary(orderQuantity,
                        unitPrice, OrderType.SELL);

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO);
        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(310), Currency.getInstance("GBP")), OrderUnit.KILO);
        OrderSummary orderSummary2 = new OrderSummary(orderQuantity2,
                        unitPrice2, OrderType.SELL);

        // When
        OrderSummary mergedOrder = orderSummary.merge(orderSummary2);

        // Then
        assertThat(mergedOrder.getUnitPrice(), is(unitPrice2));
        assertThat(mergedOrder.getOrderQuantity(), is(new OrderQuantity(new BigDecimal(9.5), OrderUnit.KILO)));
    }
}
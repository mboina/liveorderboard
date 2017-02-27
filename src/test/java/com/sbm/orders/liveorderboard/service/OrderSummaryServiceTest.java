package com.sbm.orders.liveorderboard.service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;
import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderQuantity;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.OrderUnit;
import com.sbm.orders.liveorderboard.domain.Price;
import com.sbm.orders.liveorderboard.domain.OrderSummary;
import com.sbm.orders.liveorderboard.domain.UnitPrice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class OrderSummaryServiceTest {

    private static final Currency GBP = Currency.getInstance("GBP");
    private OrderSummaryService orderSummaryService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        orderSummaryService = new OrderSummaryServiceImpl();
    }

    @Test
    public void orderSummaryShouldBeEmptyWhenNoOrdersAvailable() throws Exception {
        // Given there are no orders

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList());

        // Then
        assertThat(orderSummary.isEmpty(), is(true));
    }

    @Test
    public void shouldFailWhenGivenLiveOrdersIsNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("live orders cannot be null");

        // Given when null is provided
        orderSummaryService.getSummaryOrders(null);

        // Then exception thrown
    }

    @Test
    public void orderSummaryShouldBeThatOfASingleOrderWhenOneOrderIsProvided() throws Exception {
        // Given
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO))
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order));

        // Then
        assertThat(orderSummary.isEmpty(), is(false));
        assertThat(orderSummary.size(), is(1));
    }

    @Test
    public void orderSummaryShouldHaveTheDataMatchingTheGivenLiveOrder() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order));

        // Then
        assertThat(orderSummary.get(0).getOrderQuantity(), is(orderQuantity));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice));
        assertThat(orderSummary.get(0).getOrderType(), is(OrderType.SELL));
    }

    @Test
    public void orderSummaryShouldNotAddOrdersOfDifferentType() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order orderSell = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.SELL)
                        .build();

        Order orderBuy = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.BUY)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(orderSell, orderBuy));

        // Then
        assertThat(orderSummary.size(), is(2));
    }

    @Test
    public void orderSummaryShouldNotAddOrdersOfDifferentUnitPrice() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.SELL)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(310), GBP), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2));

        // Then
        assertThat(orderSummary.size(), is(2));
    }

    @Test
    public void orderSummaryShouldAddOrdersOfSameUnitPriceAndSameOrderType() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.SELL)
                        .build();

        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2));

        // Then
        assertThat(orderSummary.size(), is(1));
        assertThat(orderSummary.get(0).getOrderType(), is(OrderType.SELL));
        OrderQuantity resultOrderQuantity = new OrderQuantity(new BigDecimal(5.5), OrderUnit.KILO);
        assertThat(orderSummary.get(0).getOrderQuantity(), is(resultOrderQuantity));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice));
    }

    @Test
    public void shouldSortSellOrdersDescendingByUnitPrice() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.SELL)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(290), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.SELL)
                        .build();

        UnitPrice unitPrice3 = new UnitPrice(new Price(new BigDecimal(310), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity3 = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        Order order3 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity3)
                        .withUnitPrice(unitPrice3)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2, order3));

        // Then
        assertThat(orderSummary.size(), is(3));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice2));
        assertThat(orderSummary.get(1).getUnitPrice(), is(unitPrice1));
        assertThat(orderSummary.get(2).getUnitPrice(), is(unitPrice3));
    }

    @Test
    public void shouldSortBuyOrdersAscendingByUnitPrice() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(290), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice3 = new UnitPrice(new Price(new BigDecimal(310), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity3 = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        Order order3 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity3)
                        .withUnitPrice(unitPrice3)
                        .withOrderType(OrderType.BUY)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2, order3));

        // Then
        assertThat(orderSummary.size(), is(3));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice3));
        assertThat(orderSummary.get(1).getUnitPrice(), is(unitPrice1));
        assertThat(orderSummary.get(2).getUnitPrice(), is(unitPrice2));
    }

    @Test
    public void sellOrdersShouldBeSortedBeforeBuyOrders() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(290), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2));

        // Then
        assertThat(orderSummary.size(), is(2));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice2));
        assertThat(orderSummary.get(1).getUnitPrice(), is(unitPrice1));
    }

    @Test
    public void sellOrdersShouldBePlacedBeforeBuyOrdersAndSellOrderSortedByAscendingAndBuyOrdersSortedByDescendingOrder() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(290), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.SELL)
                        .build();

        UnitPrice unitPrice3 = new UnitPrice(new Price(new BigDecimal(306), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity3 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO);
        Order order3 = new Order.Builder()
                        .withUserId("user3")
                        .withOrderQuantity(orderQuantity3)
                        .withUnitPrice(unitPrice3)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice4 = new UnitPrice(new Price(new BigDecimal(295), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity4 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order4 = new Order.Builder()
                        .withUserId("user4")
                        .withOrderQuantity(orderQuantity4)
                        .withUnitPrice(unitPrice4)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2, order3, order4));

        // Then
        assertThat(orderSummary.size(), is(4));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice2));
        assertThat(orderSummary.get(1).getUnitPrice(), is(unitPrice4));
        assertThat(orderSummary.get(2).getUnitPrice(), is(unitPrice3));
        assertThat(orderSummary.get(3).getUnitPrice(), is(unitPrice1));
    }

    @Test
    public void sellOrdersAndBuyOrdersShouldBeMergedAndSorted() throws Exception {
        // Given
        OrderQuantity orderQuantity1 = new OrderQuantity(new BigDecimal(2.5), OrderUnit.KILO);
        UnitPrice unitPrice1 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order1 = new Order.Builder()
                        .withUserId("user1")
                        .withOrderQuantity(orderQuantity1)
                        .withUnitPrice(unitPrice1)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice2 = new UnitPrice(new Price(new BigDecimal(290), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity2 = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        Order order2 = new Order.Builder()
                        .withUserId("user2")
                        .withOrderQuantity(orderQuantity2)
                        .withUnitPrice(unitPrice2)
                        .withOrderType(OrderType.SELL)
                        .build();

        UnitPrice unitPrice3 = new UnitPrice(new Price(new BigDecimal(306), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity3 = new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO);
        Order order3 = new Order.Builder()
                        .withUserId("user3")
                        .withOrderQuantity(orderQuantity3)
                        .withUnitPrice(unitPrice3)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice4 = new UnitPrice(new Price(new BigDecimal(295), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity4 = new OrderQuantity(new BigDecimal(3.0), OrderUnit.KILO);
        Order order4 = new Order.Builder()
                        .withUserId("user4")
                        .withOrderQuantity(orderQuantity4)
                        .withUnitPrice(unitPrice4)
                        .withOrderType(OrderType.SELL)
                        .build();

        OrderQuantity orderQuantity5 = new OrderQuantity(new BigDecimal(4.5), OrderUnit.KILO);
        UnitPrice unitPrice5 = new UnitPrice(new Price(new BigDecimal(305), GBP), OrderUnit.KILO);
        Order order5 = new Order.Builder()
                        .withUserId("user5")
                        .withOrderQuantity(orderQuantity5)
                        .withUnitPrice(unitPrice5)
                        .withOrderType(OrderType.BUY)
                        .build();

        UnitPrice unitPrice6 = new UnitPrice(new Price(new BigDecimal(295), GBP), OrderUnit.KILO);
        OrderQuantity orderQuantity6 = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        Order order6 = new Order.Builder()
                        .withUserId("user6")
                        .withOrderQuantity(orderQuantity6)
                        .withUnitPrice(unitPrice6)
                        .withOrderType(OrderType.SELL)
                        .build();

        // When
        List<OrderSummary> orderSummary = orderSummaryService.getSummaryOrders(Lists.newArrayList(order1, order2, order3, order4, order5, order6));

        // Then
        assertThat(orderSummary.size(), is(4));
        assertThat(orderSummary.get(0).getUnitPrice(), is(unitPrice2));
        assertThat(orderSummary.get(0).getOrderQuantity(), is(orderQuantity2));

        assertThat(orderSummary.get(1).getUnitPrice(), is(unitPrice4));
        assertThat(orderSummary.get(1).getOrderQuantity(), is(new OrderQuantity(new BigDecimal(4.0), OrderUnit.KILO)));

        assertThat(orderSummary.get(2).getUnitPrice(), is(unitPrice3));
        assertThat(orderSummary.get(2).getOrderQuantity(), is(orderQuantity3));

        assertThat(orderSummary.get(3).getUnitPrice(), is(unitPrice1));
        assertThat(orderSummary.get(3).getOrderQuantity(), is(new OrderQuantity(new BigDecimal(7.0), OrderUnit.KILO)));
    }
}
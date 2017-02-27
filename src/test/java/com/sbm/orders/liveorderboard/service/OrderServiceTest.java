package com.sbm.orders.liveorderboard.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderQuantity;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.OrderUnit;
import com.sbm.orders.liveorderboard.domain.Price;
import com.sbm.orders.liveorderboard.domain.UnitPrice;
import com.sbm.orders.liveorderboard.exceptions.OrderNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class OrderServiceTest {
    private static final Currency GBP = Currency.getInstance("GBP");
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
    }

    @Test
    public void registerOrderShouldAddAnOrder() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(306), GBP), OrderUnit.KILO);

        // When
        orderService.registerOrder("userA", orderQuantity, unitPrice, OrderType.BUY);

        // Then
        Collection<Order> liveOrders = orderService.getLiveOrders();
        assertThat(liveOrders.isEmpty(), is(false));
        assertThat(liveOrders.size(), is(1));
    }

    @Test
    public void cancelOrderShouldRemoveTheOrder() throws Exception {
        // Given
        OrderQuantity orderQuantity = new OrderQuantity(new BigDecimal(1.0), OrderUnit.KILO);
        UnitPrice unitPrice = new UnitPrice(new Price(new BigDecimal(306), GBP), OrderUnit.KILO);
        Long orderId = orderService.registerOrder("userA", orderQuantity, unitPrice, OrderType.BUY);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Collection<Order> liveOrders = orderService.getLiveOrders();
        assertThat(liveOrders.isEmpty(), is(true));
    }

    @Test(expected = OrderNotFoundException.class)
    public void shouldThrowOrderNotFoundExceptionWhenOrderToCancelDoesNotExist() throws Exception {
        // Given order is not registered

        // When
        orderService.cancelOrder(1L);

        // Then verify exception is thrown
    }
}
package com.sbm.orders.liveorderboard.domain;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

public class OrderTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenBuildingOrderWithoutUserId() throws Exception {
        // Given when
        new Order.Builder()
                        .withOrderQuantity(Mockito.mock(OrderQuantity.class))
                        .withOrderType(OrderType.BUY)
                        .withUnitPrice(Mockito.mock(UnitPrice.class)).build();

        // Then fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenBuildingOrderWithoutOrderQuantity() throws Exception {
        // Given when
        new Order.Builder()
                        .withUserId("user")
                        .withOrderType(OrderType.BUY)
                        .withUnitPrice(Mockito.mock(UnitPrice.class)).build();

        // Then fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenBuildingOrderWithoutOrderType() throws Exception {
        // Given when
        new Order.Builder()
                        .withUserId("user")
                        .withOrderQuantity(Mockito.mock(OrderQuantity.class))
                        .withUnitPrice(Mockito.mock(UnitPrice.class))
                        .build();

        // Then fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenBuildingOrderWithoutUnitPrice() throws Exception {
        // Given when
        new Order.Builder()
                        .withUserId("user")
                        .withOrderQuantity(Mockito.mock(OrderQuantity.class))
                        .withOrderType(OrderType.BUY)
                        .build();

        // Then fail
    }

    @Test
    public void orderIdShouldBeGeneratedWhenBuildingOrder() throws Exception {
        // Given when
        Order order = new Order.Builder()
                        .withUserId("user")
                        .withOrderQuantity(Mockito.mock(OrderQuantity.class))
                        .withOrderType(OrderType.BUY)
                        .withUnitPrice(Mockito.mock(UnitPrice.class))
                        .build();

        // Then
        assertThat(order.getOrderId(), is(notNullValue()));
    }
}
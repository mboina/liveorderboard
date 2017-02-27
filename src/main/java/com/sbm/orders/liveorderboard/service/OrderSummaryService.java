package com.sbm.orders.liveorderboard.service;

import java.util.List;

import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderSummary;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.UnitPrice;

public interface OrderSummaryService {

    /**
     * Generates {@link OrderSummary}s from the given live {@link Order}s.
     * Orders are merged when they are of the same {@link OrderType} and
     * have the same {@link UnitPrice}
     * @param liveOrders live orders that are placed
     * @return list of summary orders
     */
    List<OrderSummary> getSummaryOrders(List<Order> liveOrders);
}

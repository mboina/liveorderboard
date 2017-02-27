package com.sbm.orders.liveorderboard.service;

import java.util.List;

import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderQuantity;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.UnitPrice;
import com.sbm.orders.liveorderboard.exceptions.OrderNotFoundException;

public interface OrderService {
    /**
     * Registers an order with given parameters.
     * @param userId user id associated with the order
     * @param orderQuantity quantity ordered
     * @param unitPrice price per unit
     * @param orderType type of the order e.g. BUY/SELL
     * @return order Id of the registered order
     */
    Long registerOrder(String userId, OrderQuantity orderQuantity, UnitPrice unitPrice, OrderType orderType);

    /**
     * GET all the live orders.
     * @return live orders
     */
    List<Order> getLiveOrders();

    /**
     * Cancel an existing order.
     * @param orderId order id to cancel
     * @throws OrderNotFoundException thrown when trying to cancel an order that does not exist
     */
    void cancelOrder(Long orderId) throws OrderNotFoundException;
}

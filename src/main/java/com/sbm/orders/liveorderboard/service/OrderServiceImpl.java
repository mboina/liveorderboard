package com.sbm.orders.liveorderboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderQuantity;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.UnitPrice;
import com.sbm.orders.liveorderboard.exceptions.OrderNotFoundException;

public class OrderServiceImpl implements OrderService {
    private Map<Long, Order> orders = new ConcurrentHashMap<>();

    public Long registerOrder(String userId, OrderQuantity orderQuantity, UnitPrice unitPrice, OrderType orderType) {
        Order order = new Order.Builder()
                        .withUserId(userId)
                        .withOrderQuantity(orderQuantity)
                        .withUnitPrice(unitPrice)
                        .withOrderType(orderType)
                        .build();

        orders.putIfAbsent(order.getOrderId(), order);
        return order.getOrderId();
    }

    public List<Order> getLiveOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public void cancelOrder(Long orderId) throws OrderNotFoundException {
        if (orders.containsKey(orderId)) {
            orders.remove(orderId);
        } else {
            throw new OrderNotFoundException("order with id [" + orderId + "] is not found");
        }
    }
}

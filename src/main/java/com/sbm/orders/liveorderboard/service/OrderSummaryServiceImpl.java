package com.sbm.orders.liveorderboard.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.sbm.orders.liveorderboard.domain.Order;
import com.sbm.orders.liveorderboard.domain.OrderType;
import com.sbm.orders.liveorderboard.domain.OrderSummary;
import com.sbm.orders.liveorderboard.domain.UnitPrice;

import static com.sbm.orders.liveorderboard.domain.OrderSummaryUnitPriceComparators.PRICE_ASCE_COMPARATOR;
import static com.sbm.orders.liveorderboard.domain.OrderSummaryUnitPriceComparators.PRICE_DESC_COMPARATOR;

public class OrderSummaryServiceImpl implements OrderSummaryService {

    @Override
    public List<OrderSummary> getSummaryOrders(List<Order> liveOrders) {
        Preconditions.checkArgument(liveOrders != null, "live orders cannot be null");

        List<OrderSummary> sellOrders = mergeOrdersByUnitPrice(liveOrders, OrderType.SELL);
        List<OrderSummary> buyOrders = mergeOrdersByUnitPrice(liveOrders, OrderType.BUY);

        List<OrderSummary> summaryOrders = sellOrders.stream().sorted(PRICE_ASCE_COMPARATOR).collect(Collectors.toList());
        summaryOrders.addAll(buyOrders.stream().sorted(PRICE_DESC_COMPARATOR).collect(Collectors.toList()));
        return summaryOrders;
    }

    private List<OrderSummary> mergeOrdersByUnitPrice(List<Order> liveOrders, OrderType orderType) {
        Map<UnitPrice, List<OrderSummary>> ordersByUnitPrice = liveOrders.stream()
                        .filter(order -> order.getOrderType() == orderType)
                        .map(order -> new OrderSummary(order.getOrderQuantity(), order.getUnitPrice(), order.getOrderType()))
                        .collect(Collectors.groupingBy(OrderSummary::getUnitPrice));

        return ordersByUnitPrice
                        .values()
                        .stream()
                        .map(a -> a.stream().reduce(OrderSummary::merge).get())
                        .collect(Collectors.toList());
    }
}

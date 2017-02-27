package com.sbm.orders.liveorderboard.domain;

import java.util.Comparator;

public class OrderSummaryUnitPriceComparators {

    public static final Comparator<OrderSummary> PRICE_ASCE_COMPARATOR = Comparator.comparing(order -> order.getUnitPrice()
                    .getPrice().getAmount());

    public static final Comparator<OrderSummary> PRICE_DESC_COMPARATOR = (OrderSummary order1, OrderSummary order2) -> order2.getUnitPrice()
                    .getPrice().getAmount().compareTo(order1.getUnitPrice().getPrice().getAmount());

}

package com.sbm.orders.liveorderboard.domain;

import com.google.common.base.Preconditions;

/**
 * Represents an {@link Order}, used for generating Order Summary.
 */
public class OrderSummary {
    private final OrderQuantity orderQuantity;
    private final OrderType orderType;
    private final UnitPrice unitPrice;

    public OrderSummary(OrderQuantity orderQuantity, UnitPrice unitPrice, OrderType orderType) {
        this.orderQuantity = orderQuantity;
        this.unitPrice = unitPrice;
        this.orderType = orderType;
    }

    public OrderQuantity getOrderQuantity() {
        return orderQuantity;
    }

    public UnitPrice getUnitPrice() {
        return unitPrice;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderSummary merge(OrderSummary that) {
        OrderUnit thisOrderUnit = this.getOrderQuantity().getOrderUnit();
        OrderUnit thatOrderUnit = that.getOrderQuantity().getOrderUnit();
        Preconditions.checkArgument(thisOrderUnit == thatOrderUnit,
                        "Cannot merge as order units do not match [" + thisOrderUnit + "]  != [" + thisOrderUnit + "]");

        Preconditions.checkArgument(this.getUnitPrice().equals(that.getUnitPrice()),
                        "Cannot merge as unit prices do not match [" + this.getOrderType() + "] != [" + that.getOrderType() + "]");

        Preconditions.checkArgument(this.getOrderType() == that.getOrderType(),
                        "Cannot merge as order types do not match [" + this.getOrderType() + "] != [" + that.getOrderType() + "]");

        OrderQuantity orderQuantity = new OrderQuantity(this.orderQuantity.getQuantity().add(that.getOrderQuantity().getQuantity()),
                        thisOrderUnit);
        return new OrderSummary(orderQuantity, that.getUnitPrice(), that.getOrderType());
    }
}

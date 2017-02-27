package com.sbm.orders.liveorderboard.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Class to encapsulate order quantity and the unit of quantity.
 */
public class OrderQuantity {
    private final BigDecimal quantity;
    private final OrderUnit orderUnit;

    public OrderQuantity(BigDecimal quantity, OrderUnit orderUnit) {
        this.quantity = quantity.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.orderUnit = orderUnit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public OrderUnit getOrderUnit() {
        return orderUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderQuantity that = (OrderQuantity) o;
        return Objects.equals(quantity, that.quantity) &&
                        orderUnit == that.orderUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, orderUnit);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                        .add("quantity", quantity)
                        .add("orderUnit", orderUnit)
                        .toString();
    }
}

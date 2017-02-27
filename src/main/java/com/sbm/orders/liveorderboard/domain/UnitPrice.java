package com.sbm.orders.liveorderboard.domain;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Represents the unit price of an instrument or commodity.
 */
public class UnitPrice {
    private final Price price;
    private final OrderUnit unit;

    public UnitPrice(Price price, OrderUnit unit) {
        this.price = price;
        this.unit = unit;
    }

    public Price getPrice() {
        return price;
    }

    public OrderUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UnitPrice unitPrice = (UnitPrice) obj;
        return Objects.equals(price, unitPrice.price) &&
                        unit == unitPrice.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, unit);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                        .add("price", price)
                        .add("unit", unit)
                        .toString();
    }

}
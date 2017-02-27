package com.sbm.orders.liveorderboard.domain;

import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Domain object representing an Order
 */
public class Order {
    private static final AtomicLong orderKey = new AtomicLong(0);
    private final Long orderId;
    private final String userId;
    private final OrderQuantity orderQuantity;
    private final UnitPrice unitPrice;
    private final OrderType orderType;

    public Order(Builder builder) {
        this.orderId = orderKey.incrementAndGet();
        this.userId = builder.userId;
        this.orderQuantity = builder.orderQuantity;
        this.unitPrice = builder.unitPrice;
        this.orderType = builder.orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
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

    public static class Builder {
        private String userId;
        private OrderQuantity orderQuantity;
        private UnitPrice unitPrice;
        private OrderType orderType;

        public Builder() {
            // intentionally left blank
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withOrderQuantity(OrderQuantity orderQuantity) {
            this.orderQuantity = orderQuantity;
            return this;
        }

        public Builder withUnitPrice(UnitPrice unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder withOrderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Order build() {
            validateOrder();
            return new Order(this);
        }

        private void validateOrder() {
            checkArgument(userId != null && !userId.isEmpty(), "user id cannot be empty");
            checkArgument(orderQuantity != null, "order quantity cannot be empty");
            checkArgument(unitPrice != null, "unit price cannot be empty");
            checkArgument(orderType != null, "order type cannot be empty");
        }
    }
}

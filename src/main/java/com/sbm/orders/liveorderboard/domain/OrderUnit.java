package com.sbm.orders.liveorderboard.domain;

/**
 * Enum Representing a unit of weight.
 */
public enum OrderUnit {
    KILO("kg"),
    POUND("lb");

    private final String value;

    OrderUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

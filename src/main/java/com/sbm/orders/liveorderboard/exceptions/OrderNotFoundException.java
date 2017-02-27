package com.sbm.orders.liveorderboard.exceptions;

/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends Exception {
    /**
     * Wrapped with exception message
     * @param message
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}

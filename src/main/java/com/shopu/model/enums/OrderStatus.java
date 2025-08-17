package com.shopu.model.enums;

public enum OrderStatus {
    CONFIRMED,        // Order confirmed by seller
    SHIPPED,          // Shipped by delivery partner
    DELIVERED,        // Order delivered to customer
    CANCELLED,        // Order cancelled by user or system
    RETURNED,         // Order returned by customer
    REFUNDED
}

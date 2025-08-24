package com.shopu.model.enums;

public enum OrderStatus {
    CONFIRMED,        // Order confirmed by seller
    SHIPPED,          // Shipped by delivery partner
    CANCELLED,        // Order cancelled by user or system
    DELIVERED,        // Order delivered to customer
    RETURNED,         // Order returned by customer
    REFUNDED
}

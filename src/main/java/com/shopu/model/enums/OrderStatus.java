package com.shopu.model.enums;

public enum OrderStatus {
    PENDING,          // Order placed but not yet processed
    CONFIRMED,        // Order confirmed by seller
    PACKED,           // Order packed and ready to ship
    SHIPPED,          // Shipped by delivery partner
    OUT_FOR_DELIVERY, // Delivery agent out with order
    DELIVERED,        // Order delivered to customer
    CANCELLED,        // Order cancelled by user or system
    RETURNED,         // Order returned by customer
    REFUNDED
}

package com.shopu.model.entities;
import com.shopu.model.enums.OrderStatus;
import com.shopu.model.enums.PaymentMode;
import com.shopu.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private String orderId;
    private OrderStatus orderStatus = OrderStatus.CONFIRMED;
    private List<CartItem> cartItems;
    // Coupon Attributes
    private int couponDiscountAmount;
    private String couponCode;
    // Amount Attributes
    private float totalItemPrice;
    private float totalItemPriceWithDiscount;
    private int deliveryCharge;
    private int handlingCharge;
    private int smallCartCharge;
    private double orderAmount;
    // Payment Attributes
    private PaymentMode paymentMode = PaymentMode.COD;
    private float amountPaidOnline;
    private float codAmountPending;
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    private String paymentId;
    // Delivery attributes
    private Address address;
    private LocalDateTime deliveredAt;
    private boolean refundInitiated = false;
    private String deliveryNotes;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    // User ID of those who update something in order
    // private String updatedBy;
}

package com.shopu.model.entities;
import com.shopu.model.enums.OrderStatus;
import com.shopu.model.enums.PaymentMode;
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
    private PaymentMode paymentMode;
    private List<CartItem> cartItems;
    private double orderAmount;
    private String address;
    private OrderStatus orderStatus = OrderStatus.CONFIRMED;
    private String paymentId;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}

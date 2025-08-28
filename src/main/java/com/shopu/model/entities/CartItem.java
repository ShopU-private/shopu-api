package com.shopu.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    private String id;
    private String userId;
    private String productId;
    private String imageUrl;
    private String productName;
    private double price;
    private double discountedPrice;
    private int buyQuantity;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}

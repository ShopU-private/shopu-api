package com.shopu.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @Id
    private String id;

    @Indexed(unique = true)
    private String code;                // e.g., FIRST100, AFTER3, AFTER10
    private String title;               // e.g., ₹100 Off on First Order
    private String description;         // e.g., Get ₹100 off on your very first order with us. No coupon needed!

    private int discountAmount;
    private int discountPercentage;
    private int minOrderAmount;

    private int requiredOrderCount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active;
    @Field(name = "isUpTo")
    private boolean isUpTo;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}

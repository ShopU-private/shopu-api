package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CouponCreateRequest {
    private String code;                // e.g., FIRST100, AFTER3, AFTER10
    private String title;               // e.g., ₹100 Off on First Order
    private String description;         // e.g., Get ₹100 off on your very first order with us. No coupon needed!

    private int discountAmount;
    private int discountPercentage;
    private int minOrderAmount;

    private int requiredOrderCount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isUpTo;
}

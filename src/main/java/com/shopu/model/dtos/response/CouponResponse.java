package com.shopu.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CouponResponse {
    private String code;
    private int minOrderAmount;
    private int discountAmount;
    private int requiredOrderCount;
}

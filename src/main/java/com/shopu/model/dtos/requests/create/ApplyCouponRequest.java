package com.shopu.model.dtos.requests.create;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplyCouponRequest {
    @NotNull
    @Size(min = 10, max = 50, message = "Invalid user")
    private String userId;

    @NotNull
    @Size(min = 10, max = 50, message = "Invalid coupon")
    private String couponCode;

    @NotNull(message = "Order Amount is required")
    @Digits(integer = 10, fraction = 6)
    @DecimalMin(value = "0.0", message = "Order Amount should not be negative")
    private float orderAmount;
}

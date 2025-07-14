package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreateOrderRequest {
    private String userId;
    private PaymentMode paymentMode;
    private double orderAmount;
    private String addressId;
    private String paymentId;
}

package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderRequest {
    private String userId;
    private List<String> cartItemIds;
    private PaymentMode paymentMode;
    private double orderAmount;
    private String address;
    private String paymentId;
}

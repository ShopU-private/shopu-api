package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "User Id required")
    private String userId;
    private PaymentMode paymentMode;
    @NotNull(message = "OrderAmount required")
    private double totalItemPrice;
    private double totalItemPriceWithDiscount;
    private int deliveryCharge;
    private int handlingCharge;
    private int smallCartCharge;
    private double orderAmount;
    @NotNull(message = "Payment Id required")
    private String paymentId;
    @NotNull(message = "Address Id required")
    private String addressId;
}
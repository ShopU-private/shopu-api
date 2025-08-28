package com.shopu.model.dtos.response.order;

import com.shopu.model.entities.Address;
import com.shopu.model.enums.OrderStatus;
import com.shopu.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderListResponseApp {
    private String id;
    private String orderId;
    private int totalItem;
    private float orderAmount;
    private float amountPaidOnline;
    private float codAmountPending;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private Address address;
    private LocalDateTime createdAt;
}

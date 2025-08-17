package com.shopu.model.dtos.response;

import com.shopu.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderListResponse {
    private String id;
    private String orderId;
    private String receiverName;
    private int totalItem;
    private float orderAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}

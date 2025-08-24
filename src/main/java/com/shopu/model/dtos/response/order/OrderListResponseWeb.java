package com.shopu.model.dtos.response.order;

import com.shopu.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderListResponseWeb {
    private String id;
    private String orderId;
    private String receiverName;
    private int totalItem;
    private float orderAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}

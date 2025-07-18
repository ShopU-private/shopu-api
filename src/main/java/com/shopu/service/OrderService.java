package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.entities.Order;
import org.springframework.data.domain.Page;

public interface OrderService {
    ApiResponse<Order> placeOrder(CreateOrderRequest orderRequest);

    ApiResponse<Page<Order>> fetchOrder(String userId, int page, int size);

    ApiResponse<Page<Order>> allOrders(int page, int size);

    ApiResponse<Order> updateOrderStatus(String status, String orderId);
}

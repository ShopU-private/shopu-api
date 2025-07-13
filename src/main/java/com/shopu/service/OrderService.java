package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.entities.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    ApiResponse<Order> createOrder(CreateOrderRequest orderRequest);

    ApiResponse<List<Order>> fetchOrder(String userId);

    ApiResponse<Page<Order>> allOrders(int page, int size);

    ApiResponse<Order> updateOrderStatus(String status, String orderId);
}

package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.dtos.response.OrderListResponse;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.entities.Order;

public interface OrderService {
    ApiResponse<Order> placeOrder(CreateOrderRequest orderRequest);

    ApiResponse<PagedResponse<Order>> fetchOrder(String userId, int page, int size);

    ApiResponse<PagedResponse<OrderListResponse>> allOrders(int page, int size);

    ApiResponse<Order> updateOrderStatus(String status, String orderId);
}

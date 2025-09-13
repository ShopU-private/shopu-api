package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.order.OrderListResponseApp;
import com.shopu.model.dtos.response.order.OrderListResponseWeb;
import com.shopu.model.entities.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ApiResponse<Order> placeOrder(CreateOrderRequest orderRequest);

    ApiResponse<PagedResponse<Order>> fetchOrder(String userId, int page, int size);

    ApiResponse<Boolean> updateOrderStatus(String status, String id);

    ApiResponse<Boolean> updatePaymentStatus(String id);

    ApiResponse<PagedResponse<OrderListResponseApp>> fetchOrdersApp(int page, int size);

    ApiResponse<List<OrderListResponseApp>> searchOrders(String query);

    ApiResponse<Long> getNoOfAllOrders();

    ApiResponse<Map<String, Long>> getSaleSummary();

    ApiResponse<PagedResponse<OrderListResponseWeb>> fetchOrdersWeb(int page, int size, String status);

    ApiResponse<List<OrderListResponseWeb>> searchOrdersWeb(String id);
}

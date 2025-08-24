package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.order.OrderListResponseApp;
import com.shopu.model.dtos.response.order.OrderListResponseWeb;
import com.shopu.model.entities.Order;
import com.shopu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placed")
    public ResponseEntity<ApiResponse<Order>> placeOrder(@RequestBody CreateOrderRequest orderRequest){
        ApiResponse<Order> response = orderService.placeOrder(orderRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/fetch/{userId}/{page}/{size}")
    public ResponseEntity<ApiResponse<PagedResponse<Order>>> fetchOrder(@PathVariable String userId, @PathVariable int page, @PathVariable int size){
        ApiResponse<PagedResponse<Order>> response = orderService.fetchOrder(userId, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<ApiResponse<PagedResponse<OrderListResponseApp>>> fetchOrdersApp(@PathVariable int page, @PathVariable int size){
        ApiResponse<PagedResponse<OrderListResponseApp>> response = orderService.fetchOrdersApp(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<OrderListResponseApp>>> searchOrders(@RequestParam String query){
        ApiResponse<List<OrderListResponseApp>> response = orderService.searchOrders(query);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/order-status")
    public ResponseEntity<ApiResponse<Boolean>> updateOrderStatus(@RequestParam String id, @RequestParam String status){
        ApiResponse<Boolean> response = orderService.updateOrderStatus(status, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/payment-status")
    public ResponseEntity<ApiResponse<Boolean>> updatePaymentStatus(@RequestParam String id){
        ApiResponse<Boolean> response = orderService.updatePaymentStatus(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

/// Web APIs

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all/web/{page}/{size}")
    public ResponseEntity<ApiResponse<PagedResponse<OrderListResponseWeb>>> fetchOrdersWeb(@PathVariable int page, @PathVariable int size){
        ApiResponse<PagedResponse<OrderListResponseWeb>> response = orderService.fetchOrdersWeb(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}

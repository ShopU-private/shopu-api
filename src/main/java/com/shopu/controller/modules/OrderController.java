package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.entities.Order;
import com.shopu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Page<Order>>> fetchOrder(@PathVariable String userId, @PathVariable int page, @PathVariable int size){
        ApiResponse<Page<Order>> response = orderService.fetchOrder(userId, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<ApiResponse<Page<Order>>> allOrders(@PathVariable int page, @PathVariable int size){
        ApiResponse<Page<Order>> response = orderService.allOrders(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@RequestParam String status, @RequestParam String orderId){
        ApiResponse<Order> response = orderService.updateOrderStatus(status, orderId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

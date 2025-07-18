package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.entities.Address;
import com.shopu.model.entities.CartItem;
import com.shopu.model.entities.Order;
import com.shopu.model.entities.User;
import com.shopu.model.enums.OrderStatus;
import com.shopu.repository.OrderRepository;
import com.shopu.service.AddressService;
import com.shopu.service.CartItemService;
import com.shopu.service.OrderService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressService addressService;

    @Override
    public ApiResponse<Order> placeOrder(CreateOrderRequest orderRequest) {
        User user = userService.findById(orderRequest.getUserId());
        if(user == null){
            throw new ApplicationException("User not found");
        }
        Address address = addressService.findById(orderRequest.getAddressId());
        if(address == null){
            throw new ApplicationException("Address not found");
        }
        if(user.getCartItemsId().isEmpty()){
            throw new ApplicationException("Cart is Empty");
        }
        List<CartItem> cartItems = cartItemService.fetchCartItems(user.getCartItemsId());
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setPaymentMode(orderRequest.getPaymentMode());
        order.setPaymentId(orderRequest.getPaymentId());
        order.setCartItems(cartItems);
        order.setOrderAmount(orderRequest.getOrderAmount());
        order.setAddress(address);
        String id = orderRepository.save(order).getId();

        userService.updateOrder(user.getId(), id);
        cartItemService.deleteCartItems(user.getCartItemsId());

        return new ApiResponse<>(orderRepository.save(order), HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<Page<Order>> fetchOrder(String userId, int page, int size) {
        User user = userService.findById(userId);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new ApiResponse<>(orderRepository.findAllByUserId(userId, pageable), HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<Order>> allOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders = orderRepository.findAll(pageable);
        return new ApiResponse<>(orders, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Order> updateOrderStatus(String status, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ApplicationException("Order not found"));
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setOrderStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        return new ApiResponse<>(orderRepository.save(order), HttpStatus.OK);
    }
}

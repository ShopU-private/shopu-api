package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.entities.Address;
import com.shopu.model.entities.CartItem;
import com.shopu.model.entities.Order;
import com.shopu.model.entities.User;
import com.shopu.model.enums.OrderStatus;
import com.shopu.model.enums.PaymentMode;
import com.shopu.model.enums.PaymentStatus;
import com.shopu.repository.OrderRepository;
import com.shopu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private CouponService couponService;

    @Autowired
    private RazorpayService razorpayService;

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

        Order order = getOrder(orderRequest, cartItems, address);
        String id = orderRepository.save(order).getId();

        userService.updateOrder(user.getId(), id);
        if(orderRequest.getCouponCode() != null){
            couponService.useCoupon(orderRequest.getUserId(), orderRequest.getCouponCode());
        }
        cartItemService.deleteCartItems(user.getCartItemsId());
        return new ApiResponse<>(orderRepository.save(order), HttpStatus.CREATED);
    }

    private Order getOrder(CreateOrderRequest orderRequest, List<CartItem> cartItems, Address address) {
        Order order = new Order();

        if(orderRequest.getPaymentMode() != PaymentMode.COD){
            Map<String, Object> paymentDetails = razorpayService.getPaymentDetails(orderRequest.getPaymentId());

            if (orderRequest.getPaymentMode() == null && paymentDetails.get("method") != null) {
                String method = paymentDetails.getOrDefault("method", "COD").toString().toUpperCase();
                try {
                    orderRequest.setPaymentMode(PaymentMode.valueOf(method));
                } catch (IllegalArgumentException e) {
                    orderRequest.setPaymentMode(PaymentMode.COD);
                }
            }

            float paidAmount = ((int) paymentDetails.getOrDefault("amount", 0)) / 100f;
            float orderAmount = orderRequest.getOrderAmount();

            float codPending = Math.max(orderAmount - paidAmount, 0);
            PaymentStatus paymentStatus = paidAmount == orderAmount
                    ? PaymentStatus.PAID
                    : (paidAmount > 0 ? PaymentStatus.PARTIAL_PAID : PaymentStatus.UNPAID);
            order.setAmountPaidOnline(paidAmount);
            order.setCodAmountPending(codPending);
            order.setPaymentStatus(paymentStatus);
        }else{
            order.setCodAmountPending(orderRequest.getOrderAmount());
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
        order.setUserId(orderRequest.getUserId());
        order.setOrderId(generateOrderId());
        order.setCouponDiscountAmount(orderRequest.getCouponDiscountAmount());
        order.setCouponCode(orderRequest.getCouponCode());
        order.setPaymentMode(orderRequest.getPaymentMode());
        order.setTotalItemPrice(orderRequest.getTotalItemPrice());
        order.setTotalItemPriceWithDiscount(orderRequest.getTotalItemPriceWithDiscount());
        order.setDeliveryCharge(orderRequest.getDeliveryCharge());
        order.setHandlingCharge(orderRequest.getHandlingCharge());
        order.setSmallCartCharge(orderRequest.getSmallCartCharge());
        order.setOrderAmount(orderRequest.getOrderAmount());
        order.setPaymentId(orderRequest.getPaymentId());
        order.setCartItems(cartItems);
        order.setAddress(address);
        return order;
    }

    @Override
    public ApiResponse<PagedResponse<Order>> fetchOrder(String userId, int page, int size) {
        User user = userService.findById(userId);
        if(user == null){
            throw new ApplicationException("User not found");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> response = orderRepository.findAllByUserId(userId, pageable);

        return new ApiResponse<>( new PagedResponse<>(response.getContent(), response.getNumber(),
                response.getTotalPages(), response.isLast(), response.isFirst()), HttpStatus.OK);
    }

    @Override
    public ApiResponse<PagedResponse<Order>> allOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> response = orderRepository.findAll(pageable);
        return new ApiResponse<>(new PagedResponse<>(response.getContent(), response.getNumber(),
                response.getTotalPages(), response.isLast(), response.isFirst()), HttpStatus.OK);
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


    private static String generateOrderId() {
        LocalDateTime now = LocalDateTime.now();

        String year = String.valueOf(now.getYear()).substring(2);       // last 2 digits
        String monthDay = now.format(DateTimeFormatter.ofPattern("MMdd"));
        String hour = String.valueOf(now.getHour());                    // no leading 0
        String minute = String.valueOf(now.getMinute());                // no leading 0
        String millis = String.valueOf(System.currentTimeMillis()).substring(8); // last few millis for uniqueness

        return "SUOD" + year + monthDay + hour + minute + millis;
    }
}

package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.CreateOrderRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.order.OrderListResponseApp;
import com.shopu.model.dtos.response.order.OrderListResponseWeb;
import com.shopu.model.entities.*;
import com.shopu.model.enums.OrderStatus;
import com.shopu.model.enums.PaymentMode;
import com.shopu.model.enums.PaymentStatus;
import com.shopu.repository.common.OrderRepository;
import com.shopu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    private MongoTemplate mongoTemplate;

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
        // TODO update stock of product from cartItemServiceImpl layer by this API
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
    public ApiResponse<List<OrderListResponseApp>> searchOrders(String query) {
        if (query.trim().isEmpty()) {
            return new ApiResponse<>(Collections.emptyList(), HttpStatus.OK);
        }

        MatchOperation matchStage = Aggregation.match(Criteria.where("orderId").regex(query, "i"));

        ProjectionOperation projectStage = Aggregation.project("id", "orderId", "orderAmount",
                        "amountPaidOnline", "codAmountPending", "paymentStatus",
                        "orderStatus", "address", "createdAt")
                .andExpression("size(cartItems)").as("totalItem");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage);

        List<OrderListResponseApp> result = mongoTemplate.aggregate(aggregation, "orders", OrderListResponseApp.class).getMappedResults();

        return new ApiResponse<>(result, HttpStatus.OK);
    }

    @Override
    public ApiResponse<PagedResponse<OrderListResponseApp>> fetchOrdersApp(int page, int size) {
        int skip = page * size;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.skip(skip),
                Aggregation.limit(size),
                Aggregation.project("id", "orderId", "orderAmount", "amountPaidOnline",
                                "codAmountPending", "paymentStatus", "orderStatus", "address", "createdAt")
                        .and(ArrayOperators.Size.lengthOfArray("cartItems")).as("totalItem")
        );

        List<OrderListResponseApp> orders = mongoTemplate.aggregate(
                aggregation,
                "orders",
                OrderListResponseApp.class
        ).getMappedResults();

        long total = mongoTemplate.count(new Query(), "orders");
        int totalPages = (int) Math.ceil((double) total / size);

        PagedResponse<OrderListResponseApp> pagedResponse = new PagedResponse<>(
                orders,
                page,
                totalPages,
                page >= totalPages - 1,
                page == 0
        );

        return new ApiResponse<>(pagedResponse, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Boolean> updateOrderStatus(String status, String id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ApplicationException("Order not found"));
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        OrderStatus currentStatus = order.getOrderStatus();

        if (newStatus.ordinal() < currentStatus.ordinal()) {
            throw new ApplicationException("Cannot move order status backwards");
        }

        if(newStatus.ordinal() > OrderStatus.SHIPPED.ordinal() && order.getPaymentStatus().ordinal() < PaymentStatus.PAID.ordinal()){
            throw new ApplicationException("Invalid attempt: Payment still pending");
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        return new ApiResponse<>(true, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Boolean> updatePaymentStatus(String id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ApplicationException("Order not found"));

        if(PaymentStatus.PAID == order.getPaymentStatus()){
            throw new ApplicationException("Order status already paid");
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setCodAmountPaid(order.getCodAmountPending());
        order.setCodAmountPending(0);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return new ApiResponse<>(true, HttpStatus.OK);
    }

    @Override
    public ApiResponse<PagedResponse<OrderListResponseWeb>> fetchOrdersWeb(int page, int size) {
        int skip = page * size;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.skip(skip),
                Aggregation.limit(size),
                Aggregation.project("id", "orderId", "orderAmount", "createdAt")
                        .and("orderStatus").as("status")
                        .and("address.personName").as("receiverName")
                        .and(ArrayOperators.Size.lengthOfArray("cartItems")).as("totalItem")
        );

        long total = mongoTemplate.count(new Query(), "orders");
        int totalPages = (int) Math.ceil((double) total / size);

        List<OrderListResponseWeb> orders = mongoTemplate.aggregate(
                aggregation,
                "orders",
                OrderListResponseWeb.class
        ).getMappedResults();

        PagedResponse<OrderListResponseWeb> pagedResponse = new PagedResponse<>(
                orders,
                page,
                totalPages,
                page >= totalPages - 1,
                page == 0
        );

        return new ApiResponse<>(pagedResponse, HttpStatus.OK);
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

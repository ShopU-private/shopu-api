package com.shopu.repository.common;

import com.shopu.model.dtos.response.order.OrderListResponseApp;
import com.shopu.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findAllByUserId(String userId, Pageable pageable);

    List<OrderListResponseApp> findByOrderIdContainingIgnoreCase(String query);
}

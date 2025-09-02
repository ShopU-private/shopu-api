package com.shopu.repository.common;

import com.shopu.model.entities.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {
    Coupon findByCode(String code);

    List<Coupon> findAllByActiveTrueAndEndDateAfterOrderByCreatedAtDesc(LocalDateTime now);
}

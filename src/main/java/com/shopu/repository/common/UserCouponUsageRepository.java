package com.shopu.repository.common;

import com.shopu.model.entities.UserCouponUsage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponUsageRepository extends MongoRepository <UserCouponUsage, String> {
    UserCouponUsage findByCouponCodeAndUserId(String couponCode, String userId);
}

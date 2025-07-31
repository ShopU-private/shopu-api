package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ApplyCouponRequest;
import com.shopu.model.dtos.requests.create.CouponCreateRequest;
import com.shopu.model.dtos.response.CouponResponse;
import com.shopu.model.entities.Coupon;

import java.util.List;

public interface CouponService {

    ApiResponse<CouponResponse> applyCoupon(ApplyCouponRequest request);

    ApiResponse<Coupon> createCoupon(CouponCreateRequest createRequest);

    boolean useCoupon(String userId, String couponCode);

    ApiResponse<List<Coupon>> fetchAllCoupons();

    ApiResponse<List<Coupon>> fetchAvailableCoupons(String userId);
}

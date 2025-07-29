package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.ApplyCouponRequest;
import com.shopu.model.dtos.requests.create.CouponCreateRequest;
import com.shopu.model.dtos.response.CouponResponse;
import com.shopu.model.entities.Coupon;
import com.shopu.model.entities.User;
import com.shopu.model.entities.UserCouponUsage;
import com.shopu.repository.CouponRepository;
import com.shopu.repository.UserCouponUsageRepository;
import com.shopu.service.CouponService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private UserService userService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponUsageRepository usageRepository;

    @Override
    public ApiResponse<Coupon> createCoupon(CouponCreateRequest createRequest) {
        if(couponRepository.findByCode(createRequest.getCode()) != null){
            throw new ApplicationException("Code already available in use");
        }

        Coupon coupon = new Coupon();
        coupon.setCode(createRequest.getCode());
        coupon.setTitle(createRequest.getTitle());
        coupon.setDescription(createRequest.getDescription());
        coupon.setDiscountAmount(createRequest.getDiscountAmount());
        coupon.setDiscountPercentage(createRequest.getDiscountPercentage());
        coupon.setMinOrderAmount(createRequest.getMinOrderAmount());
        coupon.setRequiredOrderCount(createRequest.getRequiredOrderCount());
        coupon.setStartDate(createRequest.getStartDate());
        coupon.setEndDate(createRequest.getEndDate());
        coupon.setActive(true);
        coupon.setUpTo(createRequest.isUpTo());

        return new ApiResponse<>(couponRepository.save(coupon), HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<CouponResponse> applyCoupon(ApplyCouponRequest request) {
        User user = userService.findById(request.getUserId());
        if(user == null){
            throw new ApplicationException("User not found");
        }

        UserCouponUsage usedCoupon = usageRepository.findByCouponCodeAndUserId(request.getCouponCode(), request.getUserId());
        if(usedCoupon != null){
            throw new ApplicationException("You already used this coupon");
        }

        Coupon coupon = couponRepository.findByCode(request.getCouponCode());
        if(coupon == null || !coupon.isActive()){
            throw new ApplicationException("Coupon not found or inactive");
        }

        // Check coupon is valid or not
        if (LocalDateTime.now().isBefore(coupon.getStartDate()) || LocalDateTime.now().isAfter(coupon.getEndDate())) {
            throw new ApplicationException("Coupon is expired or not yet valid");
        }

        // Check orderAmount is valid or not
        if (request.getOrderAmount() < coupon.getMinOrderAmount()) {
            throw new ApplicationException("Minimum order amount for this coupon is ₹" + coupon.getMinOrderAmount());
        }

        // Check orderCount required
        if(user.getOrderIds().size() < coupon.getRequiredOrderCount()){
            throw new ApplicationException("This coupon requires at least " + coupon.getRequiredOrderCount() + " past orders");
        }

        if(!coupon.isUpTo()){
            return new ApiResponse<>(new CouponResponse(coupon.getCode(),
                    coupon.getMinOrderAmount(),
                    coupon.getDiscountAmount(),
                    coupon.getRequiredOrderCount()), HttpStatus.OK);
        }

        int discountAmount = calculateDiscount(request.getOrderAmount(), coupon.getMinOrderAmount(), coupon.getDiscountAmount());
        return new ApiResponse<>(new CouponResponse(coupon.getCode(), coupon.getMinOrderAmount(), discountAmount, coupon.getRequiredOrderCount()), HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<Coupon>> fetchAllCoupons() {
        return new ApiResponse<>(couponRepository.findAllByActiveTrueOrderByCreatedAtDesc(), HttpStatus.OK);
    }

    @Override
    public boolean useCoupon(String userId, String couponCode) {
        UserCouponUsage couponUse = new UserCouponUsage();
        couponUse.setUserId(userId);
        couponUse.setCouponCode(couponCode);
        couponUse.setUsedAt(LocalDateTime.now());
        usageRepository.save(couponUse);
        return true;
    }

    public int calculateDiscount(double orderAmount, double minOrderAmount, int couponDiscountAmount) {
        int discount = 0;
        Random random = new Random();

        double plus20 = minOrderAmount + (minOrderAmount * 0.2);
        double plus50 = minOrderAmount + (minOrderAmount * 0.5);
        double plus80 = minOrderAmount + (minOrderAmount * 0.8);
        double plus90 = minOrderAmount + (minOrderAmount * 0.9);
        double plus100 = minOrderAmount * 2.0;

        if (orderAmount >= plus100) {
            discount = couponDiscountAmount; // Full discount
        } else if (orderAmount >= plus90) {
            discount = Math.round(couponDiscountAmount * 0.9f);
        } else if (orderAmount >= plus80) {
            discount = getRandomDiscount(couponDiscountAmount, 70, 80, random);
        } else if (orderAmount >= plus50) {
            discount = getRandomDiscount(couponDiscountAmount, 50, 60, random);
        } else if (orderAmount >= plus20) {
            discount = getRandomDiscount(couponDiscountAmount, 30, 40, random);
        } else if (orderAmount >= minOrderAmount) {
            discount = getRandomDiscount(couponDiscountAmount, 20, 30, random);
        }

        return Math.min(discount, couponDiscountAmount);
    }

    private int getRandomDiscount(int baseAmount, int minPercent, int maxPercent, Random random) {
        int percent = minPercent + random.nextInt(maxPercent - minPercent + 1);
        double rawDiscount = (percent / 100.0) * baseAmount;
        return Math.round((float) rawDiscount); // Round to nearest int
    }
}

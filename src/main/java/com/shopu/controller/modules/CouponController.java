package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ApplyCouponRequest;
import com.shopu.model.dtos.requests.create.CouponCreateRequest;
import com.shopu.model.dtos.response.CouponResponse;
import com.shopu.model.entities.Coupon;
import com.shopu.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(@RequestBody CouponCreateRequest createRequest){
        ApiResponse<Coupon> response = couponService.createCoupon(createRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/apply")
    public ResponseEntity<ApiResponse<CouponResponse>> applyCoupon(@RequestBody ApplyCouponRequest request){
        ApiResponse<CouponResponse> response = couponService.applyCoupon(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse<Coupon>> updateCoupon(){
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Coupon>>> fetchAllCoupons(){
        ApiResponse<List<Coupon>> response = couponService.fetchAllCoupons();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

package com.shopu.service;


import com.shopu.common.utils.ApiResponse;

public interface EmailService {

    ApiResponse<String> sendOtp(String email);
}

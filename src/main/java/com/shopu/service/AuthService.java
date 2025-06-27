package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.LoginRequest;
import com.shopu.model.dtos.response.AuthResponse;

import java.util.Map;

public interface AuthService {
    ApiResponse<AuthResponse> verifiedLogin(LoginRequest loginRequest);

    ApiResponse<AuthResponse> refreshToken(Map<String, String> request);
    

}
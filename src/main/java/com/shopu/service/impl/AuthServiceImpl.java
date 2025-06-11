package com.shopu.service.impl;

import com.shopu.authentication.JwtUtil;
import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.LoginRequest;
import com.shopu.model.dtos.response.AuthResponse;
import com.shopu.model.entities.User;
import com.shopu.service.AuthService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<User> login(LoginRequest loginRequest) {
        if(!Objects.equals(loginRequest.getOtp(), "1234")){
            return new ApiResponse<>("Incorrect OTP", HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUser(loginRequest.getPhoneNumber());
        String token = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        userService.updateLastSignIn(user.getId());
        return new ApiResponse<>(user, HttpStatus.OK, token);
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(Map<String, String> request) {
        String token = request.get("token");

        if (!jwtUtil.validateAccessToken(token)) {
            return new ApiResponse<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = jwtUtil.refreshAccessToken(token);
        return new ApiResponse<>(new AuthResponse(newAccessToken), HttpStatus.OK);
    }
}

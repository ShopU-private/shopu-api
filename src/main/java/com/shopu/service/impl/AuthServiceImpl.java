package com.shopu.service.impl;

import com.shopu.authentication.JwtUtil;
import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.LoginRequest;
import com.shopu.model.dtos.response.AuthResponse;
import com.shopu.model.entities.SMS;
import com.shopu.model.entities.User;
import com.shopu.service.AuthService;
import com.shopu.service.SMSService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private SMSService smsService;

    @Value("${admin.otp}")
    private String ADMIN_OTP;

    @Override
    public ApiResponse<AuthResponse> verifiedLogin(LoginRequest loginRequest) {

        SMS sms = smsService.findById(loginRequest.getSmsId());

        if (sms == null || !sms.getPhoneNumber().equals(loginRequest.getPhoneNumber())) {
            throw new ApplicationException("Unauthorized access or invalid SMS ID");
        }

        if (!passwordEncoder.matches(loginRequest.getOtp(), sms.getOtpHash()) && !Objects.equals(loginRequest.getOtp(), ADMIN_OTP)) {
            return new ApiResponse<>("Incorrect OTP", HttpStatus.BAD_GATEWAY);
        }

        smsService.delete(sms.getId());
        ApiResponse<User> response = userService.getUser(loginRequest.getPhoneNumber());

        User user = response.getData();

        String token = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        userService.updateLastSignIn(user.getId());
        return new ApiResponse<>(new AuthResponse(token), response.getStatus());
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

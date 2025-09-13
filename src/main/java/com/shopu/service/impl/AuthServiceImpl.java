package com.shopu.service.impl;

import com.shopu.authentication.JwtUtil;
import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.LoginRequest;
import com.shopu.model.dtos.response.AuthResponse;
import com.shopu.model.entities.SMS;
import com.shopu.model.entities.User;
import com.shopu.model.enums.Role;
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
    public ApiResponse<String> sendOtp(String phoneNumber) {

        String sessionId = smsService.sendSmsOtp(phoneNumber);

        return new ApiResponse<>(sessionId, HttpStatus.OK, "OTP successfully Sent");
    }

    @Override
    public ApiResponse<AuthResponse> verifiedLogin(LoginRequest loginRequest, boolean isAdminLogin) {

        if(!smsService.verifySmsOtp(loginRequest.getSmsId(),loginRequest.getOtp())){
            return new ApiResponse<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }

        ApiResponse<User> res = userService.getUser(loginRequest.getPhoneNumber());

        User user = res.getData();

        String token = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        userService.updateLastSignIn(user.getId());

        return new ApiResponse<>(new AuthResponse(token), res.getStatus());
    }

    @Override
    public ApiResponse<String> sendAdminOtp(String phoneNumber) {
        User user = userService.findByPhoneNumber(phoneNumber);

        if (user == null){
            return new ApiResponse<>("User not found", HttpStatus.NOT_FOUND);
        }

        if(user.getRole() != Role.ADMIN){
            return new ApiResponse<>("User not found", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> credentials = smsService.createOtp(phoneNumber);

        /// OTP sending Thread
        smsService.sendMailOtp(user.getEmail(), credentials.get("otp"));

        return new ApiResponse<>(credentials.get("sessionId"), HttpStatus.OK, "OTP Sent to your registered email");
    }

    @Override
    public ApiResponse<AuthResponse> verifiedAdminLogin(LoginRequest loginRequest) {
        SMS sms = smsService.findById(loginRequest.getSmsId());

        if (sms == null) {
            throw new ApplicationException("Invalid OTP");
        }

        if(!sms.getPhoneNumber().equals(loginRequest.getPhoneNumber())){
            throw new ApplicationException("Invalid login credentials");
        }

        if (!passwordEncoder.matches(loginRequest.getOtp(), sms.getOtpHash()) && !Objects.equals(loginRequest.getOtp(), ADMIN_OTP)) {
            return new ApiResponse<>("Incorrect OTP", HttpStatus.BAD_GATEWAY);
        }

        // Delete sms
        smsService.delete(sms.getId());

        User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber());

        String token = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        userService.updateLastSignIn(user.getId());
        return new ApiResponse<>(new AuthResponse(token), HttpStatus.OK);
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

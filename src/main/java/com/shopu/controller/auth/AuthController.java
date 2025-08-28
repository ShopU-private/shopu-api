package com.shopu.controller.auth;
import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.LoginRequest;
import com.shopu.model.dtos.response.AuthResponse;
import com.shopu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> response = authService.verifiedLogin(loginRequest, false);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/admin-login")
    public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(@RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> response = authService.verifiedLogin(loginRequest, true);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestParam String phoneNumber) {
        ApiResponse<String> response = authService.sendOtp(phoneNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendExistingOtp(@RequestParam String smsId, @RequestParam String phoneNumber){
        ApiResponse<String> response = authService.resendOtp(smsId, phoneNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody Map<String, String> request) {
        ApiResponse<AuthResponse> response = authService.refreshToken(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

package com.shopu.controller.messaging;

import com.shopu.common.utils.ApiResponse;
import com.shopu.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "*")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestParam String email) {
        ApiResponse<String> response = emailService.sendOtp(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

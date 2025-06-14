package com.shopu.controller.messaging;

import com.shopu.common.utils.ApiResponse;
import com.shopu.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@CrossOrigin(origins = "*")
public class SMSController {

    @Autowired
    private SMSService smsService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestParam String phoneNumber) {
        ApiResponse<String> response = smsService.sendOtp(phoneNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}


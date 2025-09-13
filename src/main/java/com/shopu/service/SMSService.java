package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.SMS;

import java.util.Map;

public interface SMSService {

    Map<String, String> createOtp(String email);

    SMS findById(String smsId);

    void delete(String smsId);

    void sendMailOtp(String email, String otp);

    String sendSmsOtp(String phoneNumber);

    boolean verifySmsOtp(String sessionId, String otp);

}

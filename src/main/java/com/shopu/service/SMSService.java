package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.SMS;

import java.util.Map;

public interface SMSService {

    Map<String, String> createOtp(String phoneNumber);

    void sendOtp(String otp);

    void sendSMS(String otp, String phoneNumber);

    SMS findById(String smsId);

    void delete(String smsId);

}

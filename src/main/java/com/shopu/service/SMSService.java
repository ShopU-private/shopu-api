package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.entities.SMS;

public interface SMSService {

    ApiResponse<String> sendOtp(String phoneNumber);

    SMS findById(String smsId);

    void delete(String smsId);
}

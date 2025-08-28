package com.shopu.service;

import java.util.Map;
public interface RazorpayService {
    Map<String, Object> getPaymentDetails(String paymentId);
}

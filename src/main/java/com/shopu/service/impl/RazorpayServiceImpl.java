package com.shopu.service.impl;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.shopu.service.RazorpayService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RazorpayServiceImpl implements RazorpayService {
    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Override
    public Map<String, Object> getPaymentDetails(String paymentId) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);
            Payment payment = client.payments.fetch(paymentId);
            JSONObject json = payment.toJson();
            return json.toMap();
        } catch (Exception e) {
            return Map.of("error", "Failed to fetch payment details", "message", e.getMessage());
        }
    }

//    public boolean verifyPayment(String orderId, String paymentId, String signature) {
//        try {
//            String generatedSignature = Utils.verifyPaymentSignature(new JSONObject() {{
//                put("razorpay_order_id", orderId);
//                put("razorpay_payment_id", paymentId);
//                put("razorpay_signature", signature);
//            }}, razorpaySecret);
//
//            return generatedSignature != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}

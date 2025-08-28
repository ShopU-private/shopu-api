//package com.shopu.service.impl;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shopu.common.utils.ApiResponse;
//import com.shopu.service.PaymentService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    @Value("${cashfree.app.id}")
//    private String APP_ID;
//
//    @Value("${cashfree.secret.key}")
//    private String SECRET_KEY;
//
//    @Override
//    public ApiResponse<Map<String, String>> generateQR(String orderId, String phoneNumber, float amount) {
//        try{
//            final String baseUrl = "https://api.cashfree.com/pg";
//            String body = getQrGenerateBody(orderId, phoneNumber, amount);
//
//            HttpClient client = HttpClient.newHttpClient();
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(baseUrl + "/orders"))
//                    .header("Content-Type", "application/json")
//                    .header("x-client-id", APP_ID)
//                    .header("x-client-secret", SECRET_KEY)
//                    .header("x-api-version", "2022-09-01")
//                    .POST(HttpRequest.BodyPublishers.ofString(body))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode json = mapper.readTree(response.body());
//
//            Map<String, String> resp = new HashMap<>();
////            resp.put("orderId", orderId);
////            resp.put("paymentSessionId", json.get("payment_session_id").asText());
////            resp.put("upiQrCode", json.get("payment_links").get("upi_qr").asText());
//
//            System.out.println(json);
//            return new ApiResponse<>(resp, HttpStatus.OK);
//        } catch (IOException | InterruptedException e) {
//            if (e instanceof InterruptedException) {
//                Thread.currentThread().interrupt();
//            }
//            return new ApiResponse<>("Failed to generate QR", HttpStatus.FAILED_DEPENDENCY);
//        }
//    }
//
//    private String getQrGenerateBody(String orderId, String phoneNumber, float amount) {
//        String amt = String.format(Locale.US, "%.2f", amount);
//
//        String body = "{"
//                + "\"order_amount\": " + amt + ","
//                + "\"order_currency\": \"INR\","
//                + "\"order_id\": \"" + orderId + "\","
//                + "\"customer_details\": {"
//                + "     \"customer_id\": \"CUST_" + phoneNumber + "\","
//                + "     \"customer_phone\": \""+ phoneNumber +"\""
//                + "},"
//                + "\"order_meta\": {"
//                + "     \"payment_methods\": \"upi\""
//                + "}"
//                + "}";
//        return body;
//    }
//}

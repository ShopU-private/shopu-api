//package com.shopu.controller.payment;
//
//import com.shopu.common.utils.ApiResponse;
//import com.shopu.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("api/v1/payment")
//@CrossOrigin(origins = "*")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @PostMapping("/generate-qr")
//    public ResponseEntity<ApiResponse<Map<String, String>>> generateQR(
//            @RequestParam String orderId, @RequestParam String phoneNumber, @RequestParam float amount){
//        ApiResponse<Map<String, String>> response = paymentService.generateQR(orderId, phoneNumber, amount);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//}

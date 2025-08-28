package com.shopu.model.entities;

import com.shopu.model.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String paymentId;
    private float amount;
    private PaymentMode paymentMode;
    private LocalDateTime timestamp = LocalDateTime.now();
}

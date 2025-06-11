package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    private String phoneNumber;
    private String otp;
}


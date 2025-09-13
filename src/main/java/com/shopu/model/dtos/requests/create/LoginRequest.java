package com.shopu.model.dtos.requests.create;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    @Digits(fraction = 0, integer = 10, message = "Invalid phone number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number Invalid")
    private String phoneNumber;

    @NotNull
    @Digits(integer = 6, fraction = 0, message = "Invalid OTP format")
    private String otp;

    @NotNull
    @Size(min = 20, max = 50, message = "Invalid SMS id")
    private String smsId;
}


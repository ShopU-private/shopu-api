package com.shopu.model.dtos.requests.update;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProfileRequest {
    @NotNull(message = "Name cannot empty")
    @Size(min = 3, max = 50, message = "Invalid name")
    private String name;

    @NotNull(message = "Email cannot empty")
    @Size(min = 3, max = 50, message = "Invalid E-mail")
    private String email;

    @NotNull(message = "Name cannot empty")
    @Digits(fraction = 0, integer = 10, message = "Invalid Whatsapp number")
    private String whatsappNumber;
}

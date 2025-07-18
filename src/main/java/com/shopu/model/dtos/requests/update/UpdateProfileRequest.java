package com.shopu.model.dtos.requests.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private String email;
    private String whatsappNumber;
}

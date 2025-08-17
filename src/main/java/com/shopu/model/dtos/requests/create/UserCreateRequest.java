package com.shopu.model.dtos.requests.create;

import com.shopu.model.entities.Address;
import com.shopu.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateRequest {
    @NotNull(message = "Name is required")
    private String name;

    private String email;

    @NotNull(message = "PhoneNumber is required")
    private String phoneNumber;

    private String whatsappNumber;

    @NotNull(message = "Role is required")
    private Role role;
}

package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.AddressType;
import com.shopu.model.enums.Role;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotNull(message = "userId is cannot be empty/null")
    @Size(min = 10, max = 50, message = "Invalid user")
    private String userId;

    @NotNull(message = "addressType is cannot be empty/null")
    private AddressType addressType;

    @NotNull(message = "personName is cannot be empty/null")
    @Size(min = 2, max = 50, message = "Invalid PersonName")
    private String personName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number Invalid")
    @NotNull(message = "phoneNumber is cannot be empty/null")
    private String phoneNumber;

    @NotNull(message = "houseNumber is cannot be empty/null")
    @Size(min = 1, max = 100, message = "Invalid House/flat")
    private String houseNumber;

    @NotNull(message = "street is cannot be empty/null")
    @Size(min = 1, max = 150, message = "Invalid Street")
    private String street;

    @Size(min = 1, max = 100, message = "Invalid landmark")
    private String landmark;

    @NotNull(message = "city is cannot be empty/null")
    @Size(min = 1, max = 50, message = "Invalid city")
    private String city;

    @NotNull(message = "state is cannot be empty/null")
    @Size(min = 1, max = 50, message = "Invalid state")
    private String state;

    @NotNull(message = "pinCode is cannot be empty/null")
    @Digits(integer = 10, fraction = 0, message = "Invalid Pin-code")
    private String pinCode;

    @NotNull(message = "latitude is cannot be empty/null")
    @Size(min = 1, max = 50, message = "Invalid latitude")
    private String latitude;

    @NotNull(message = "longitude is cannot be empty/null")
    @Size(min = 1, max = 50, message = "Invalid longitude")
    private String longitude;
}

package com.shopu.model.dtos.requests.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateRequest {
    private String addressId;
    private String personName;
    private String phoneNumber;
    private String houseNumber;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String pinCode;
}

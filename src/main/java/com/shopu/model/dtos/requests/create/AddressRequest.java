package com.shopu.model.dtos.requests.create;

import com.shopu.model.enums.AddressType;
import com.shopu.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressRequest {
    private String userId;
    private AddressType addressType;
    private String personName;
    private String phoneNumber;

    private String houseNumber;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String pinCode;
}

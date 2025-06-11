package com.shopu.model.entities;

import com.shopu.model.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    private String id;
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

    private boolean isDefault;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

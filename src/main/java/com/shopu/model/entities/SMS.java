package com.shopu.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class SMS {
    @Id
    private String id;
    private String phoneNumber;
    private String otpHash;

    public SMS(String phoneNumber, String otpHash) {
        this.phoneNumber = phoneNumber;
        this.otpHash = otpHash;
    }
}

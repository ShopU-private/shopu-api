package com.shopu.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

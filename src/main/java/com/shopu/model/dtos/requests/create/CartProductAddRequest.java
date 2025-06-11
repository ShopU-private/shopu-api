package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartProductAddRequest {
    private String userId;
    private String productId;
    private int quantity;
}

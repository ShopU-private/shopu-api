package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemAddRequest {
    private String userId;
    private String productId;
    private String imageUrl;
    private String productName;
    private double price;
    private double discount;
    private int buyQuantity;
}

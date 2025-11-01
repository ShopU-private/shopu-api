package com.shopu.model.dtos.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDTO {
    private String productId;
    private String imageUrl;
    private String productName;
    private double price;
    private double discountedPrice;
    private int buyQuantity;
}

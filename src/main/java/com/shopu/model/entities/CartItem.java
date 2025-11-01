package com.shopu.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String productId;
    private String imageUrl;
    private String productName;
    private double price;
    private double discountedPrice;
    private int buyQuantity;
}

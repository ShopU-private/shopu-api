package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CartItemAddRequest;
import com.shopu.model.entities.CartItem;

import java.util.List;

public interface CartItemService {

    ApiResponse<CartItem> addToCart(CartItemAddRequest addRequest);

    ApiResponse<Boolean> removeFromCart(String userId, String cartItemId);

    ApiResponse<List<CartItem>> fetchCartItem(String userId);
}

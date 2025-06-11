package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CartProductAddRequest;
import com.shopu.model.entities.CartProduct;

public interface CartProductService {

    ApiResponse<CartProduct> addToCart(CartProductAddRequest addRequest);

    ApiResponse<Boolean> removeFromCart(String userId, String cartProductId);
}

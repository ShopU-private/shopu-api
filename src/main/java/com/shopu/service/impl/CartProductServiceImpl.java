package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.CartProductAddRequest;
import com.shopu.model.entities.CartProduct;
import com.shopu.repository.CartProductRepository;
import com.shopu.service.CartProductService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class CartProductServiceImpl implements CartProductService {
    @Autowired
    private UserService userService;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Override
    public ApiResponse<CartProduct> addToCart(CartProductAddRequest addRequest) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setUserId(addRequest.getUserId());
        cartProduct.setProductId(addRequest.getProductId());
        cartProduct.setQuantity(addRequest.getQuantity());
        String id = cartProductRepository.save(cartProduct).getId();
        userService.updateCart(addRequest.getUserId(), addRequest.getProductId(), true);
        return new ApiResponse<>(cartProduct, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Boolean> removeFromCart(String userId, String cartProductId) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId).orElse(null);
        if(cartProduct == null){
            throw new ApplicationException("Product not found");
        }
        userService.updateCart(userId, cartProduct.getId(), false);
        cartProductRepository.delete(cartProduct);
        return new ApiResponse<>(true, HttpStatus.OK);
    }
}

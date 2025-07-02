package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.CartItemAddRequest;
import com.shopu.model.entities.CartItem;
import com.shopu.model.entities.User;
import com.shopu.repository.CartItemRepository;
import com.shopu.service.CartItemService;
import com.shopu.service.ProductService;
import com.shopu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public ApiResponse<CartItem> addToCart(CartItemAddRequest addRequest) {
        CartItem cartItem = new CartItem(
                null,
                addRequest.getUserId(),
                addRequest.getProductId(),
                addRequest.getImageUrl(),
                addRequest.getProductName(),
                addRequest.getPrice() * addRequest.getBuyQuantity(),
                (addRequest.getPrice() - addRequest.getDiscount()) * addRequest.getBuyQuantity(),
                addRequest.getBuyQuantity(),
                LocalDateTime.now(),
                null
        );

        String id = cartItemRepository.save(cartItem).getId();
        userService.updateCart(addRequest.getUserId(), id, true);

        return new ApiResponse<>(cartItem, HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<Boolean> removeFromCart(String userId, String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if(cartItem == null){
            throw new ApplicationException("Product not found");
        }
        userService.updateCart(userId, cartItemId, false);
        cartItemRepository.delete(cartItem);
        return new ApiResponse<>(true, HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<CartItem>> fetchCartItem(String userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new ApplicationException("User not found");
        }
        List<String> ids = user.getCart_items_id(); // IDs of CartProduct
        if (ids == null || ids.isEmpty()) {
            return new ApiResponse<>("Cart is empty", HttpStatus.BAD_REQUEST);
        }
        List<CartItem> cartItems = cartItemRepository.findAllById(ids);
        return new ApiResponse<>(cartItems, HttpStatus.OK);
    }

}


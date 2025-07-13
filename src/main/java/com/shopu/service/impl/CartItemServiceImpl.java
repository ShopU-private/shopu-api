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
        User user = userService.findById(addRequest.getUserId());
        if(user == null){
            throw new ApplicationException("User not found");
        }

        List<CartItem> cartItems = cartItemRepository.findAllById(user.getCartItemsId());

        for (CartItem item : cartItems){
            if(item.getProductId().equals(addRequest.getProductId())){
                item.setPrice(item.getPrice() + (addRequest.getPrice() * addRequest.getBuyQuantity()));
                item.setDiscountedPrice(item.getDiscountedPrice() + ((addRequest.getPrice() - addRequest.getDiscount()) * addRequest.getBuyQuantity()));
                item.setBuyQuantity(addRequest.getBuyQuantity() + item.getBuyQuantity());
                item.setUpdatedAt(LocalDateTime.now());
                return new ApiResponse<>(cartItemRepository.save(item), HttpStatus.CREATED);
            }
        }

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
        List<String> ids = user.getCartItemsId(); // IDs of CartProduct
        if (ids == null || ids.isEmpty()) {
            return new ApiResponse<>("Cart is empty", HttpStatus.BAD_REQUEST);
        }
        List<CartItem> cartItems = cartItemRepository.findAllById(ids);
        return new ApiResponse<>(cartItems, HttpStatus.OK);
    }

    @Override
    public List<CartItem> fetchCartItems(List<String> cartItemIds) {
        return cartItemRepository.findAllById(cartItemIds);
    }

    @Override
    public Boolean deleteCartItems(List<String> cartItemIds) {
        cartItemRepository.deleteAllById(cartItemIds);
        return true;
    }
}


package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CartItemAddRequest;
import com.shopu.model.entities.CartItem;
import com.shopu.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/cart-products")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(@RequestBody CartItemAddRequest addRequest){
        ApiResponse<CartItem> response = cartItemService.addToCart(addRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Boolean>> removeFromCart(@RequestParam String userId, @RequestParam String cartItemId){
        ApiResponse<Boolean> response = cartItemService.removeFromCart(userId, cartItemId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<List<CartItem>>> fetchCartItem(@RequestParam String userId){
        ApiResponse<List<CartItem>> response = cartItemService.fetchCartItem(userId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

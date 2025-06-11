package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.CartProductAddRequest;
import com.shopu.model.entities.CartProduct;
import com.shopu.service.CartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/cart-products")
public class CartProductController {

    @Autowired
    private CartProductService cartProductService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartProduct>> addToCart(@RequestBody CartProductAddRequest addRequest){
        ApiResponse<CartProduct> response = cartProductService.addToCart(addRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Boolean>> removeFromCart(@RequestParam String userId, @RequestParam String cartProductId){
        ApiResponse<Boolean> response = cartProductService.removeFromCart(userId, cartProductId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

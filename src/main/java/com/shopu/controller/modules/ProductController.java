package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.entities.Product;
import com.shopu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Boolean>> addProduct(@RequestBody ProductCreateRequest createRequest){
        ApiResponse<Boolean> response = productService.addProduct(createRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<List<Product>>> fetchAllProduct(){
        ApiResponse<List<Product>> response = productService.fetchAllProduct();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

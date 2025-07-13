package com.shopu.controller.modules;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.dtos.requests.update.ProductUpdateRequest;
import com.shopu.model.entities.Product;
import com.shopu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Boolean>> addProduct(@RequestBody ProductCreateRequest createRequest){
        ApiResponse<Boolean> response = productService.addProduct(createRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update-product")
    public ResponseEntity<ApiResponse<Boolean>> updateProduct(@RequestBody ProductUpdateRequest updateRequest){
        ApiResponse<Boolean> response = productService.updateProduct(updateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/remove-product/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeProduct(@PathVariable String id){
        ApiResponse<Boolean> response = productService.removeProduct(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/fetch/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> fetchAllProduct(@PathVariable String category){
        ApiResponse<List<Product>> response = productService.fetchAllProduct(category);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String query){
        ApiResponse<List<Product>> response = productService.searchProducts(query);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

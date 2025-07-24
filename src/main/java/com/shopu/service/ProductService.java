package com.shopu.service;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.dtos.requests.update.ProductUpdateRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.entities.Product;

import java.util.List;

public interface ProductService {

    ApiResponse<Boolean> addProduct(ProductCreateRequest createRequest);

    ApiResponse<PagedResponse<Product>> fetchProduct(String category, int page, int size);

    ApiResponse<Boolean> updateProduct(ProductUpdateRequest updateRequest);

    ApiResponse<Boolean> removeProduct(String id);

    ApiResponse<List<Product>> searchProducts(String query);

    Product findById(String id);
}

package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.entities.Product;
import com.shopu.repository.ProductRepository;
import com.shopu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ApiResponse<Boolean> addProduct(ProductCreateRequest createRequest) {
        Product product = new Product();
        product.setBrand(createRequest.getBrand());
        product.setName(createRequest.getName());
        product.setDescription(createRequest.getDescription());
        product.setPrice(createRequest.getPrice());
        product.setDiscount(createRequest.getDiscount());
        product.setImages(new ArrayList<>());
        product.setImages(createRequest.getImages());
        productRepository.save(product);
        return new ApiResponse<>(true, HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<List<Product>> fetchAllProduct() {
        return new ApiResponse<>(productRepository.findAll(), HttpStatus.OK);
    }
}

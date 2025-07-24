package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.dtos.requests.update.ProductUpdateRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.entities.Product;
import com.shopu.model.enums.Category;
import com.shopu.repository.ProductRepository;
import com.shopu.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse<Boolean> addProduct(ProductCreateRequest createRequest) {
        Product product = modelMapper.map(createRequest, Product.class);
        productRepository.save(product);
        return new ApiResponse<>(true, HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<Boolean> updateProduct(ProductUpdateRequest updateRequest) {
        Product product = productRepository.findById(updateRequest.getProductId()).orElse(null);
        if(product == null){
            throw new ApplicationException("Product not found");
        }
        modelMapper.map(updateRequest, product);
        productRepository.save(product);
        return new ApiResponse<>(true, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Boolean> removeProduct(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            throw new ApplicationException("Product not found!");
        }
        productRepository.delete(product);
        return new ApiResponse<>(true, HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<Product>> searchProducts(String query) {
        if (query.trim().isEmpty()) {
            return new ApiResponse<>(Collections.emptyList(), HttpStatus.OK);
        }
        List<Product> result = productRepository
                .findByNameContainingIgnoreCase(query);
        return new ApiResponse<>(result, HttpStatus.OK);
    }

    @Override
    public ApiResponse<PagedResponse<Product>> fetchProduct(String category, int page, int size) {
        Category c = Category.valueOf(category.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> response = productRepository.findAllByCategory(c, pageable);
        PagedResponse<Product> pagedResponse = new PagedResponse<>(
                response.getContent(),
                response.getNumber(),
                response.getTotalPages(),
                response.isLast(),
                response.isFirst()
        );
        return new ApiResponse<>(pagedResponse, HttpStatus.OK);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}

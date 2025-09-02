package com.shopu.service.impl;

import com.shopu.common.utils.ApiResponse;
import com.shopu.exception.ApplicationException;
import com.shopu.model.dtos.requests.create.ProductCreateRequest;
import com.shopu.model.dtos.requests.update.ProductUpdateRequest;
import com.shopu.model.dtos.response.PagedResponse;
import com.shopu.model.dtos.response.ProductListResponse;
import com.shopu.model.entities.Product;
import com.shopu.model.enums.Category;
import com.shopu.repository.product.ProductRepository;
import com.shopu.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    private MongoTemplate mongoTemplate;

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
        List<Product> result = productRepository.findByNameContainingIgnoreCase(query);
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
    public ApiResponse<PagedResponse<ProductListResponse>> fetchAllProducts(int page, int size) {
       int skip = page * size;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.skip(skip),
                Aggregation.limit(size),
                Aggregation.project("id", "name", "description", "category", "stock", "createdAt")
                        .and(ArrayOperators.ArrayElemAt.arrayOf("images").elementAt(0)).as("image")
                        .and(ArithmeticOperators.Subtract.valueOf("price").subtract("discount")).as("price")
        );
        List<ProductListResponse> products = mongoTemplate.aggregate(
                aggregation,
                "product",
                ProductListResponse.class
        ).getMappedResults();

        long total = mongoTemplate.count(new Query(), Product.class);
        int totalPages = (int) Math.ceil((double) total / size);

        PagedResponse<ProductListResponse> pagedResponse = new PagedResponse<>(
                products,
                page,
                totalPages,
                page >= totalPages - 1,
                page == 0
        );
        return new ApiResponse<>(pagedResponse, HttpStatus.OK);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}

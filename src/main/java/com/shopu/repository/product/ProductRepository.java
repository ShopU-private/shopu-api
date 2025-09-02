package com.shopu.repository.product;

import com.shopu.model.entities.Product;
import com.shopu.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByNameContainingIgnoreCase(String query);

    Page<Product> findAllByCategory(Category c, Pageable pageable);
}

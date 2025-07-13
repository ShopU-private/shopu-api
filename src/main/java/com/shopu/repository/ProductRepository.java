package com.shopu.repository;

import com.shopu.model.entities.Product;
import com.shopu.model.enums.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByNameContainingIgnoreCase(String query);

    List<Product> findAllByCategory(Category c);
}

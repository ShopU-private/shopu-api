package com.shopu.repository;

import com.shopu.model.entities.CartProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends MongoRepository<CartProduct, String> {
}

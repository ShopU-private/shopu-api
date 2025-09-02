package com.shopu.repository.common;

import com.shopu.model.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByPhoneNumber(String phoneNumber);
}

package com.shopu.repository.common;

import com.shopu.model.entities.SMS;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSRepository extends MongoRepository<SMS, String> {
}

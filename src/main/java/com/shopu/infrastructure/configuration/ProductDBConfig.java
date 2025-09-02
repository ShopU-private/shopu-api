//package com.shopu.infrastructure.configuration;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(
//        basePackages = "com.shopu.repository.product",
//        mongoTemplateRef = "productMongoTemplate"
//)
//public class ProductDBConfig {
//
//    @Value("${shopu_product_db}")
//    public String SHOPU_PRODUCT_DB;
//
//    @Bean
//    public MongoClient defaultMongoClient() {
//        return MongoClients.create(SHOPU_PRODUCT_DB);
//    }
//
//    @Bean
//    public MongoDatabaseFactory productFactory(){
//        return new SimpleMongoClientDatabaseFactory(defaultMongoClient(), "ShopU");
//    }
//
//    @Bean(name = "productMongoTemplate")
//    public MongoTemplate productMongoTemplate(MongoDatabaseFactory productFactory){
//        return new MongoTemplate(productFactory());
//    }
//}

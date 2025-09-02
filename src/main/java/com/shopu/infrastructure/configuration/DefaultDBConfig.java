//package com.shopu.infrastructure.configuration;
//
//import com.google.api.client.util.Value;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(
//        basePackages = "com.shopu.repository.common",
//        mongoTemplateRef = "mongoTemplate"
//)
//public class DefaultDBConfig {
//
//    @Value("${spring.data.mongodb.uri}")
//    private String defaultUri;
//
//    @Bean
//    public MongoClient defaultMongoClient() {
//        return MongoClients.create(defaultUri);
//    }
//
//    @Bean
//    public MongoDatabaseFactory defaultFactory(MongoClient defaultMongoClient) {
//        return new SimpleMongoClientDatabaseFactory(defaultMongoClient, "shopu");
//    }
//
//    @Bean(name = "mongoTemplate")
//    public MongoTemplate mongoTemplate(MongoDatabaseFactory defaultFactory) {
//        return new MongoTemplate(defaultFactory);
//    }
//}

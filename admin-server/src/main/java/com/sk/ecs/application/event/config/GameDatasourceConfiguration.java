package com.sk.ecs.application.event.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


//@SpringBootConfiguration
//@EnableMongoRepositories(basePackages = "com.sk.ecs.application.event.repository", mongoTemplateRef = "gameMongoTemplate")
public class GameDatasourceConfiguration {
//
//    @Bean("gameMongoProperties")
//    @ConfigurationProperties("spring.data.mongodb.game")
//    public MongoProperties gameMongoProperties() {
//        return new MongoProperties();
//    }
//
//    @Bean("gameMongoDatabaseFactory")
//    public MongoDatabaseFactory gameMongoDatabaseFactory(@Qualifier("gameMongoProperties") MongoProperties mongoProperties) {
//        return new SimpleMongoClientDatabaseFactory(mongoProperties.getUri() + "/" + mongoProperties.getDatabase());
//    }
//
//    @Bean("gameMongoTemplate")
//    public MongoTemplate gameMongoTemplate(@Qualifier("gameMongoDatabaseFactory") MongoDatabaseFactory gameMongoDatabaseFactory) {
//        return new MongoTemplate(gameMongoDatabaseFactory);
//    }
}

package com.sk.ecs.application.admin.config;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootConfiguration
@EnableMongoRepositories(basePackages = "com.sk.ecs.application.admin.repository", mongoTemplateRef = "adminMongoTemplate")
public class AdminDatasourceConfiguration {

    @Primary
    @Bean("adminMongoProperties")
    @ConfigurationProperties("spring.data.mongodb.admin")
    public MongoProperties adminMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean("adminMongoDatabaseFactory")
    public MongoDatabaseFactory adminMongoDatabaseFactory(@Qualifier("adminMongoProperties") MongoProperties mongoProperties) {
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();
        MongoPropertiesClientSettingsBuilderCustomizer builderCustomizer = new MongoPropertiesClientSettingsBuilderCustomizer(mongoProperties);
        builderCustomizer.customize(settingsBuilder);
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(settingsBuilder.build()), mongoProperties.getDatabase());
    }

    @Primary
    @Bean("adminMongoTemplate")
    public MongoTemplate adminMongoTemplate(@Qualifier("adminMongoDatabaseFactory") MongoDatabaseFactory gameMongoDatabaseFactory) {
        return new MongoTemplate(gameMongoDatabaseFactory);
    }
}

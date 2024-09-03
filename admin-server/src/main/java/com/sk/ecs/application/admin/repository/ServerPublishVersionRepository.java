package com.sk.ecs.application.admin.repository;

import com.sk.ecs.application.admin.entity.ServerPublishVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerPublishVersionRepository extends MongoRepository<ServerPublishVersion, String> {
}

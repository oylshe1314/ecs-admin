package com.sk.ecs.application.admin.repository;

import com.sk.ecs.application.admin.entity.ServerConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ServerConfigRepository extends MongoRepository<ServerConfig, ObjectId> {

    List<ServerConfig> findAllByState(Integer state);

    boolean existsByIdNotAndTypeIdAndAppId(ObjectId id, ObjectId typeId, Integer appId);

    boolean existsAllByTypeId(ObjectId typeId);

    boolean existsAllByHostId(ObjectId hostId);

    @Query("{_id:{$in:?0}}")
    @Update("{$set:{'state':?1, 'updateBy':?2, 'updateTime': ?3}}")
    void updateState(Set<ObjectId> ids, Integer state, String updateBy, LocalDateTime updateTime);
}

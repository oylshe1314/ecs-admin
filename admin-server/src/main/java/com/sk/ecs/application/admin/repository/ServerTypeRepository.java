package com.sk.ecs.application.admin.repository;

import com.sk.ecs.application.admin.entity.ServerType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ServerTypeRepository extends MongoRepository<ServerType, ObjectId> {
    List<ServerType> findAllByState(Integer state);

    boolean existsByIdNotAndName(ObjectId id, String name);

    @Query("{_id:{$in:?0}}")
    @Update("{$set:{'state':?1, 'updateBy':?2, 'updateTime': ?3}}")
    void updateState(Set<ObjectId> ids, Integer state, String updateBy, LocalDateTime updateTime);

    @Query("{_id:?0}")
    @Update("{$set:{'version':?1}}")
    void updateVersion(ObjectId ids, String version);
}

package com.sk.ecs.application.admin.repository;

import com.sk.ecs.application.admin.entity.ServerHost;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ServerHostRepository extends MongoRepository<ServerHost, ObjectId> {

    List<ServerHost> findAllByState(Integer state);

    boolean existsByIdNotAndName(ObjectId id, String name);

    boolean existsByIdNotAndHostAndPort(ObjectId id, String host, Integer port);

    @Query("{_id:{$in:?0}}")
    @Update("{$set:{'state':?1, 'updateBy':?2, 'updateTime': ?3}}")
    void updateState(Set<ObjectId> ids, Integer state, String updateBy, LocalDateTime updateTime);
}

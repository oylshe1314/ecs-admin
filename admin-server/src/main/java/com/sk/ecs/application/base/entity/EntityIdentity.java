package com.sk.ecs.application.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class EntityIdentity {

    @Id
    private ObjectId id;
}

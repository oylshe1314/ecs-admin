package com.sk.ecs.application.admin.entity;

import com.sk.ecs.application.base.entity.EntityStateful;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ServerConfig extends EntityStateful {

    private ObjectId typeId;

    private Integer appId;

    private ObjectId hostId;

    private String content;

    @DBRef
    private ServerType serverType;

    @DBRef
    private ServerHost serverHost;
}

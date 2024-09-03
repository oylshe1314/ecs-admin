package com.sk.ecs.application.admin.entity;

import com.sk.ecs.application.base.entity.EntityStateful;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ServerType extends EntityStateful {

    private String name;

    private String version;

    private String description;
}

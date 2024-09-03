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
public class Admin extends EntityStateful {

    private ObjectId roleId;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String email;

    private String mobile;

    @DBRef
    private Role role;
}

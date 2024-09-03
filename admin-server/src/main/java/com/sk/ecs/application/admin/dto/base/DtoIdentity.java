package com.sk.ecs.application.admin.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
public class DtoIdentity {

    @Schema(title = "ID")
    private final String id;

    protected DtoIdentity(ObjectId id) {
        this.id = id.toHexString();
    }
}

package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "角色查询")
public class RoleQueryDto {

    @NullOrNotBlank
    @Schema(title = "名称")
    private final String name;

    public RoleQueryDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}

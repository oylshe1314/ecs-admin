package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "服务类型查询")
public class ServerTypeQueryDto {

    @NullOrNotBlank
    @Schema(title = "主机名称")
    private final String name;

    @JsonCreator
    public ServerTypeQueryDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}

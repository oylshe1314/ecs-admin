package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(title = "服务配置查询")
public class ServerConfigQueryDto {

    @NullOrNotBlank
    @Schema(title = "服务名称")
    private final String typeId;

    @Positive
    @Schema(title = "服务ID")
    private final Integer appId;

    @JsonCreator
    public ServerConfigQueryDto(@JsonProperty("typeId") String typeId, @JsonProperty("appId") Integer appId) {
        this.typeId = typeId;
        this.appId = appId;
    }
}

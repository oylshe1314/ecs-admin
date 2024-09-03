package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "服务主机查询")
public class ServerHostQueryDto {

    @NullOrNotBlank
    @Schema(title = "主机名称")
    private final String name;

    @NullOrNotBlank
    @Schema(title = "主机地址")
    private final String host;

    @JsonCreator
    public ServerHostQueryDto(@JsonProperty("name") String name, @JsonProperty("host") String host) {
        this.name = name;
        this.host = host;
    }
}

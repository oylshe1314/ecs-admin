package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(title = "服务配置添加")
public class ServerConfigAddDto {

    @NotBlank
    @Schema(title = "服务类型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String typeId;

    @NotNull
    @Positive
    @Schema(title = "服务ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer appId;

    @NotBlank
    @Schema(title = "主机ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String hostId;

    @NotBlank
    @Schema(title = "配置内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String content;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerConfigAddDto(@JsonProperty("typeId") String typeId,
                              @JsonProperty("appId") Integer appId,
                              @JsonProperty("hostId") String hostId,
                              @JsonProperty("content") String content,
                              @JsonProperty("remark") String remark) {
        this.typeId = typeId;
        this.appId = appId;
        this.hostId = hostId;
        this.content = content;
        this.remark = remark;
    }
}

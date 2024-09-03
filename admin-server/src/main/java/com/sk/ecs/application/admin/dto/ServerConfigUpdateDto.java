package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(title = "服务配置修改")
public class ServerConfigUpdateDto {

    @NotBlank
    @Schema(title = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String id;

    @NullOrNotBlank
    @Schema(title = "服务类型ID")
    private final String typeId;

    @Positive
    @Schema(title = "服务ID")
    private final Integer appId;

    @NullOrNotBlank
    @Schema(title = "主机ID")
    private final String hostId;

    @NullOrNotBlank
    @Schema(title = "配置内容")
    private final String content;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerConfigUpdateDto(@JsonProperty("id") String id, @JsonProperty("typeId") String typeId, @JsonProperty("appId") Integer appId, @JsonProperty("hostId") String hostId, @JsonProperty("content") String content, @JsonProperty("remark") String remark) {
        this.id = id;
        this.typeId = typeId;
        this.appId = appId;
        this.hostId = hostId;
        this.content = content;
        this.remark = remark;
    }
}

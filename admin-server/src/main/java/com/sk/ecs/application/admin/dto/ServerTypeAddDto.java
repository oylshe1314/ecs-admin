package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(title = "服务主机添加")
public class ServerTypeAddDto {

    @NotBlank
    @Schema(title = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String name;

    @NotBlank
    @Schema(title = "类型描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String description;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerTypeAddDto(@JsonProperty("name") String name,
                            @JsonProperty("description") String description,
                            @JsonProperty("remark") String remark) {
        this.name = name;
        this.description = description;
        this.remark = remark;
    }
}

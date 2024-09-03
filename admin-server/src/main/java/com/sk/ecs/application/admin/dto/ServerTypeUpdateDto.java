package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "服务类型修改")
public class ServerTypeUpdateDto {

    @NotBlank
    @Schema(title = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String id;

    @NullOrNotBlank
    @Schema(title = "类型名称")
    private final String name;

    @NullOrNotBlank
    @Schema(title = "类型描述")
    private final String description;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerTypeUpdateDto(@JsonProperty("id") String id,
                               @JsonProperty("name") String name,
                               @JsonProperty("description") String description,
                               @JsonProperty("remark") String remark) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.remark = remark;
    }
}

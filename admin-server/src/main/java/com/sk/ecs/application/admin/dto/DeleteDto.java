package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "通用删除")
public class DeleteDto {

    @NotEmpty
    @Schema(name = "ids", title = "ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<String> ids;

    public DeleteDto(@JsonProperty("ids") List<String> ids) {
        this.ids = ids;
    }
}

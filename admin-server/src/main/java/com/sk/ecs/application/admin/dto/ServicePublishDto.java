package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@Schema(title = "服务发布")
public class ServicePublishDto {

    @NotEmpty
    @Schema(title = "配置ID")
    private final String configId;

    @JsonCreator
    public ServicePublishDto(@JsonProperty("configId") String configId) {
        this.configId = configId;
    }
}

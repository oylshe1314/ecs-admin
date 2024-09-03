package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.Integers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(title = "服务操作")
public class ServiceControlDto {

    @NotEmpty
    @Schema(title = "配置ID")
    private final String configId;

    @NotNull
    @Integers({1, 2, 3, 4})
    @Schema(title = "操作", description = "1.启动, 2.停止, 3.重启, 4.重载", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer operate;

    @JsonCreator
    public ServiceControlDto(@JsonProperty("configId") String configId,
                             @JsonProperty("operate") Integer operate) {
        this.configId = configId;
        this.operate = operate;
    }
}

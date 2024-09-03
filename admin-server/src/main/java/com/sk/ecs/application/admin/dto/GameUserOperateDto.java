package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.Integers;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(title = "游戏账号操作")
public class GameUserOperateDto {

    @NotEmpty
    @Schema(title = "用户ID")
    private final String userId;

    @NotNull
    @Integers({1, 2, 3, 4})
    @Schema(title = "操作", description = "1.封禁, 2.解封, 3.下线, 4.密码重置", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer operate;

    @NullOrNotBlank
    @Schema(title = "操作参数")
    private final String args;

    @JsonCreator
    public GameUserOperateDto(@JsonProperty("userId") String userId,
                              @JsonProperty("operate") Integer operate,
                              @JsonProperty("args") String args) {
        this.userId = userId;
        this.operate = operate;
        this.args = args;
    }
}

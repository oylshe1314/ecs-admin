package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.Integers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "游戏玩家操作")
public class GamePlayerOperateDto {

    @NotNull
    @Positive
    @Schema(title = "服务ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer appId;

    @NotNull
    @Positive
    @Schema(title = "玩家ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Long playerId;

    @NotNull
    @Integers({1, 2, 3})
    @Schema(title = "操作", description = "1.封禁, 2.解封, 3.下线", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer operate;

    @NotNull
    @PositiveOrZero
    @Schema(title = "操作参数")
    private final Integer args;

    @JsonCreator
    public GamePlayerOperateDto(@JsonProperty("appId") Integer appId,
                                @JsonProperty("playerId") Long playerId,
                                @JsonProperty("operate") Integer operate,
                                @JsonProperty("args") Integer args) {
        this.appId = appId;
        this.playerId = playerId;
        this.operate = operate;
        this.args = args;
    }
}

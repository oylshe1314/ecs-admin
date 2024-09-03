package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "游戏账号查询")
public class GameUserQueryDto {

    @PositiveOrZero
    @Schema(title = "渠道")
    private final Integer channel;

    @Positive
    @Schema(title = "用户ID")
    private final Long userId;

    @JsonCreator
    public GameUserQueryDto(@JsonProperty("channel") Integer channel, @JsonProperty("userId") Long userId) {
        this.channel = channel;
        this.userId = userId;
    }
}

package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "游戏玩家")
public class GamePlayerDto {

    @JsonRawValue
    @Schema(title = "玩家信息")
    private final String player;

    public GamePlayerDto(String player) {
        this.player = player;
    }
}

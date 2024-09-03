package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "后台邮件发送")
public class BackMailSendDto {

    @NullOrNotEmpty
    @Schema(title = "服务ID")
    private final List<Integer> appIds;

    @NullOrNotEmpty
    @Schema(title = "玩家ID")
    private final List<Long> playerIds;

    @NotBlank
    @Schema(title = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String title;

    @NotBlank
    @Schema(title = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String content;

    @NullOrNotEmpty
    @Schema(title = "物品ID")
    private final List<Integer> itemId;

    @NullOrNotEmpty
    @Schema(title = "数量")
    private final List<Integer> itemNum;

    @NotNull
    @PositiveOrZero
    @Schema(title = "有效天数", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer expiration;

    @JsonCreator
    public BackMailSendDto(@JsonProperty("appIds") List<Integer> appIds,
                           @JsonProperty("playerIds") List<Long> playerIds,
                           @JsonProperty("title") String title,
                           @JsonProperty("content") String content,
                           @JsonProperty("itemId") List<Integer> itemId,
                           @JsonProperty("itemNum") List<Integer> itemNum,
                           @JsonProperty("expiration") Integer expiration) {
        this.appIds = appIds;
        this.playerIds = playerIds;
        this.title = title;
        this.content = content;
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.expiration = expiration;
    }
}

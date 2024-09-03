package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "兑换码添加")
public class CdkeyAddDto {

    @PositiveOrZero
    @Schema(title = "渠道")
    private final Integer channel;

    @NotBlank
    @Schema(title = "兑换码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String key;

    @PositiveOrZero
    @Schema(title = "有效期")
    private final Integer validDays;

    @NotEmpty
    @Schema(title = "物品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<Integer> itemId;

    @NotEmpty
    @Schema(title = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<Integer> itemNum;

    @JsonCreator
    public CdkeyAddDto(@JsonProperty("channel") Integer channel,
                       @JsonProperty("key") String key,
                       @JsonProperty("validDays") Integer validDays,
                       @JsonProperty("itemId") List<Integer> itemId,
                       @JsonProperty("itemNum") List<Integer> itemNum) {
        this.channel = channel;
        this.key = key;
        this.validDays = validDays;
        this.itemId = itemId;
        this.itemNum = itemNum;
    }
}

package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "兑换码生成")
public class CdkeyGenerateDto {

    @PositiveOrZero
    @Schema(title = "渠道")
    private final Integer channel;

    @NotNull
    @Positive
    @Schema(title = "生成数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer count;

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
    public CdkeyGenerateDto(@JsonProperty("channel") Integer channel,
                            @JsonProperty("Integer") Integer count,
                            @JsonProperty("validDays") Integer validDays,
                            @JsonProperty("itemId") List<Integer> itemId,
                            @JsonProperty("itemNum") List<Integer> itemNum) {
        this.channel = channel;
        this.count = count;
        this.validDays = validDays;
        this.itemId = itemId;
        this.itemNum = itemNum;
    }
}

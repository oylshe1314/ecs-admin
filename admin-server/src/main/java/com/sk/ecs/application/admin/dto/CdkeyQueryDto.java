package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "兑换码查询")
public class CdkeyQueryDto {

    @PositiveOrZero
    @Schema(name = "name", title = "名称")
    private final Integer channel;

    @NullOrNotBlank
    @Schema(name = "name", title = "名称")
    private final String key;

    @JsonCreator
    public CdkeyQueryDto(@JsonProperty("channel") Integer channel,
                         @JsonProperty("key") String key) {
        this.channel = channel;
        this.key = key;
    }
}

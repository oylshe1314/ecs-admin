package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "选择要删除的邮件")
public class BackMailSelectedDto {

    @Schema(title = "服务ID")
    private final Integer appId;

    @Schema(title = "邮件ID")
    private final Long id;

    @JsonCreator
    public BackMailSelectedDto(@JsonProperty("appId") Integer appId,
                               @JsonProperty("id") Long id) {
        this.appId = appId;
        this.id = id;
    }
}

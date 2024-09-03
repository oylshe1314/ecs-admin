package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.entity.ServerHost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "主机选择列表")
public class ServerHostSelectDto {

    @Schema(name = "id", title = "ID")
    private final String id;

    @Schema(name = "name", title = "名称")
    private final String name;

    public ServerHostSelectDto(ServerHost serverHost) {
        this.id = serverHost.getId().toHexString();
        this.name = serverHost.getName();
    }
}

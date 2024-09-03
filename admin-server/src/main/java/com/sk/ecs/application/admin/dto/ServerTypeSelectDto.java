package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "主机选择列表")
public class ServerTypeSelectDto {

    @Schema(name = "id", title = "ID")
    private final String id;

    @Schema(name = "name", title = "名称")
    private final String name;

    public ServerTypeSelectDto(ServerType serverType) {
        this.id = serverType.getId().toHexString();
        this.name = serverType.getName();
    }
}

package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.dto.base.DtoStateful;
import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "服务类型")
public class ServerTypeDto extends DtoStateful {

    @Schema(title = "类型名称")
    private final String name;

    @Schema(title = "最新版本")
    private final String version;

    @Schema(title = "类型描述")
    private final String description;

    public ServerTypeDto(ServerType serverType) {
        super(serverType.getId(), serverType.getRemark(), serverType.getUpdateBy(), serverType.getUpdateTime(), serverType.getState());
        this.name = serverType.getName();
        this.version = serverType.getVersion();
        this.description = serverType.getDescription();
    }
}

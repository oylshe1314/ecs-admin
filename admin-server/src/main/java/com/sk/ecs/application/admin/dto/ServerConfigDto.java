package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.dto.base.DtoStateful;
import com.sk.ecs.application.admin.dto.config.ServiceConfigDto;
import com.sk.ecs.application.admin.entity.ServerConfig;
import com.sk.ecs.application.admin.util.GameChannel;
import com.sk.ecs.application.admin.util.GamePlatform;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
@Schema(title = "服务配置")
public class ServerConfigDto extends DtoStateful {

    @Schema(title = "服务ID")
    private final String typeId;

    @Schema(title = "服务类型")
    private final String typeName;

    @Schema(title = "服务ID")
    private final Integer appId;

    @Schema(title = "主机ID")
    private final String hostId;

    @Schema(title = "主机名称")
    private final String hostName;

    @Schema(title = "主机地址")
    private final String hostAddr;

    @Schema(title = "内容")
    private final String content;

    @Schema(title = "游戏服务器平台")
    private final String platform;

    @Schema(title = "游戏服务器渠道")
    private final String channel;

    @Schema(title = "游戏服务器大区")
    private final String svrArea;

    @Schema(title = "游戏服务器名称")
    private final String svrName;

    public ServerConfigDto(ServerConfig serverConfig, ServiceConfigDto configDto) {
        super(serverConfig.getId(), serverConfig.getRemark(), serverConfig.getUpdateBy(), serverConfig.getUpdateTime(), serverConfig.getState());
        this.typeId = serverConfig.getTypeId().toHexString();
        this.typeName = serverConfig.getServerType().getName();
        this.appId = serverConfig.getAppId();
        this.hostId = serverConfig.getHostId().toHexString();
        this.hostName = serverConfig.getServerHost().getName();
        this.hostAddr = serverConfig.getServerHost().getHost();
        this.content = serverConfig.getContent();
        Map<String, Object> extra = null;
        if (configDto != null && configDto.getExterServer() != null) {
            extra = configDto.getExterServer().getExtra();
        }
        if (extra == null) {
            this.platform = null;
            this.channel = null;
            this.svrArea = null;
            this.svrName = null;
        } else {
            this.platform = GamePlatform.valueOf((Integer) extra.get("platform")).getName();
            this.channel = GameChannel.valueOf((Integer) extra.get("channel")).getName();
            this.svrArea = (String) extra.get("area");
            this.svrName = (String) extra.get("name");
        }
    }
}

package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.dto.base.DtoStateful;
import com.sk.ecs.application.admin.entity.ServerHost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "服务主机")
public class ServerHostDto extends DtoStateful {

    @Schema(title = "主机名称")
    private final String name;

    @Schema(title = "主机地址")
    private final String host;

    @Schema(title = "主机端口")
    private final int port;

    @Schema(title = "服务目录")
    private final String dir;

    @Schema(title = "主机用户")
    private final String user;

    @Schema(title = "主机密码")
    private final String password;

    @Schema(title = "主机密钥")
    private final String key;

    public ServerHostDto(ServerHost serverHost) {
        super(serverHost.getId(), serverHost.getRemark(), serverHost.getUpdateBy(), serverHost.getUpdateTime(), serverHost.getState());
        this.name = serverHost.getName();
        this.host = serverHost.getHost();
        this.port = serverHost.getPort();
        this.dir = serverHost.getDir();
        this.user = serverHost.getUser();
        this.password = serverHost.getPassword();
        this.key = serverHost.getKey();
    }
}

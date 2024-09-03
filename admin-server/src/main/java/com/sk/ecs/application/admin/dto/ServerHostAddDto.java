package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "服务主机添加")
public class ServerHostAddDto {

    @NotBlank
    @Schema(title = "主机名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String name;

    @NotBlank
    @Schema(title = "主机地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String host;

    @NotNull
    @PositiveOrZero
    @Schema(title = "主机端口")
    private final Integer port;

    @NotBlank
    @Schema(title = "服务目录", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String dir;

    @NotBlank
    @Schema(title = "主机用户", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String user;

    @Schema(title = "主机密码")
    private final String password;

    @Schema(title = "主机密钥")
    private final String key;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerHostAddDto(@JsonProperty("name") String name,
                            @JsonProperty("host") String host,
                            @JsonProperty("port") int port,
                            @JsonProperty("dir") String dir,
                            @JsonProperty("user") String user,
                            @JsonProperty("password") String password,
                            @JsonProperty("key") String key,
                            @JsonProperty("remark") String remark) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.dir = dir;
        this.user = user;
        this.password = password;
        this.key = key;
        this.remark = remark;
    }
}

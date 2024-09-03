package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
@Schema(title = "服务主机修改")
public class ServerHostUpdateDto {

    @NotBlank
    @Schema(title = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String id;

    @NullOrNotBlank
    @Schema(title = "主机名称")
    private final String name;

    @NullOrNotBlank
    @Schema(title = "主机地址")
    private final String host;

    @PositiveOrZero
    @Schema(title = "主机端口")
    private final Integer port;

    @NullOrNotBlank
    @Schema(title = "服务目录")
    private final String dir;

    @NullOrNotBlank
    @Schema(title = "主机用户")
    private final String user;

    @Schema(title = "主机密码")
    private final String password;

    @Schema(title = "主机密钥")
    private final String key;

    @Schema(title = "备注")
    private final String remark;

    @JsonCreator
    public ServerHostUpdateDto(@JsonProperty("id") String id,
                               @JsonProperty("name") String name,
                               @JsonProperty("host") String host,
                               @JsonProperty("port") int port,
                               @JsonProperty("dir") String dir,
                               @JsonProperty("user") String user,
                               @JsonProperty("password") String password,
                               @JsonProperty("key") String key,
                               @JsonProperty("remark") String remark) {
        this.id = id;
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

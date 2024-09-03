package com.sk.ecs.application.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "服务信息")
public class ServiceDto extends ServiceDetectDto {

    @Schema(title = "服务配置ID")
    private final String configId;

    @Schema(title = "服务类型ID")
    private final String typeId;

    @Schema(title = "服务类型名称")
    private final String typeName;

    @Schema(title = "服务ID")
    private final Integer appId;

    @Schema(title = "服务区域")
    private final String svrArea;

    @Schema(title = "服务名称")
    private final String svrName;

    @Schema(title = "最新版本")
    private String[] newestVersion;

    @Schema(title = "发布版本")
    private String[] publishedVersion;

    public ServiceDto(String configId, String typeId, String typeName, Integer appId, String svrArea, String svrName) {
        this.configId = configId;
        this.typeId = typeId;
        this.typeName = typeName;
        this.appId = appId;
        this.svrArea = svrArea;
        this.svrName = svrName;
    }
}

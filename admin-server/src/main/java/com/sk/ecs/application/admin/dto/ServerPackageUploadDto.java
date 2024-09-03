package com.sk.ecs.application.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(title = "服务包上传")
public class ServerPackageUploadDto {

    @NotBlank
    @Schema(title = "服务类型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String typeId;

    @NotBlank
    @Schema(title = "名版本", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String version;

    @NotNull
    @Schema(title = "包文件", requiredMode = Schema.RequiredMode.REQUIRED)
    public final MultipartFile file;

    public ServerPackageUploadDto(String typeId, String version, MultipartFile file) {
        this.typeId = typeId;
        this.version = version;
        this.file = file;
    }
}

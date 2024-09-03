package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "角色菜单设置")
public class RoleSetMenusDto {

    @NotBlank
    @Schema(title = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String roleId;

    @Schema(title = "菜单ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<String> menuIds;

    public RoleSetMenusDto(@JsonProperty("roleId") String roleId,
                           @JsonProperty("menuIds") List<String> menuIds) {
        this.roleId = roleId;
        this.menuIds = menuIds;
    }
}

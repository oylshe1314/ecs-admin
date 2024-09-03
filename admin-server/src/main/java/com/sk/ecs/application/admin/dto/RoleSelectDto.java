package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "角色选择列表")
public class RoleSelectDto {

    @Schema(name = "id", title = "ID")
    private final String id;

    @Schema(name = "name", title = "名称")
    private final String name;

    public RoleSelectDto(Role role) {
        this.id = role.getId().toHexString();
        this.name = role.getName();
    }
}

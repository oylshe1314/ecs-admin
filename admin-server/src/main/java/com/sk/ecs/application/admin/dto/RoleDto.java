package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.dto.base.DtoStateful;
import com.sk.ecs.application.admin.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(title = "角色")
public class RoleDto extends DtoStateful {

    @Schema(title = "名称")
    private final String name;

    public RoleDto(Role role) {
        super(role.getId(), role.getRemark(), role.getUpdateBy(), role.getUpdateTime(), role.getState());
        this.name = role.getName();
    }
}

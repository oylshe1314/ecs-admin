package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.entity.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "菜单选择列表")
public class MenuSelectDto {

    @Schema(name = "id", title = "ID")
    private final String id;

    @Schema(name = "name", title = "名称")
    private final String name;

    public MenuSelectDto(Menu menu) {
        this.id = menu.getId().toHexString();
        this.name = menu.getName();
    }
}

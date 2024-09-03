package com.sk.ecs.application.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class MenuSortableDto<T extends MenuSortableDto<T>> {

    @Schema(name = "sortBy", title = "排序值")
    private final Integer sortBy;

    @Schema(name = "subMenus", title = "子菜单")
    private final List<T> subMenus;

    public MenuSortableDto(Integer sortBy) {
        this.sortBy = sortBy;
        this.subMenus = new ArrayList<>();
    }

    public static <T extends MenuSortableDto<T>> void sort(List<T> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        menus.sort(Comparator.comparingInt(MenuSortableDto::getSortBy));
        menus.forEach(menu -> sort(menu.getSubMenus()));
    }
}

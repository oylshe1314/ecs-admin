package com.sk.ecs.application.admin.controller;

import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.service.ServerHostService;
import com.sk.ecs.application.admin.service.ServerTypeService;
import com.sk.ecs.application.base.entity.MenuType;
import com.sk.ecs.application.common.validation.Integers;
import com.sk.ecs.application.base.entity.State;
import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.service.MenuService;
import com.sk.ecs.application.admin.service.RoleService;
import com.sk.ecs.application.common.dto.ResponseDto;
import com.sk.ecs.application.admin.entity.Menu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/common")
@Tag(name = "common", description = "公共接口")
public class CommonController {

    private final RoleService roleService;
    private final MenuService menuService;

    private final ServerTypeService serverTypeService;
    private final ServerHostService serverHostService;

    public CommonController(RoleService roleService, MenuService menuService, ServerTypeService serverTypeService, ServerHostService serverHostService) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.serverTypeService = serverTypeService;
        this.serverHostService = serverHostService;
    }

    @Operation(summary = "角色菜单")
    @RequestMapping(value = "/role/menus", method = RequestMethod.GET)
    public ResponseDto<List<RoleMenuDto>> roleMenus(Authentication authentication) {
        AdminDetails adminDetails = (AdminDetails) authentication.getPrincipal();
        List<Menu> menus = menuService.getAll(adminDetails.getRole(), Set.of(MenuType.dir, MenuType.menu));
        Map<String, RoleMenuDto> roleMenuMap = menus.stream().map(RoleMenuDto::new).collect(Collectors.toMap(RoleMenuDto::getId, roleMenuDto -> roleMenuDto));

        List<RoleMenuDto> roleMenus = new ArrayList<>();
        roleMenuMap.forEach((menuId, menu) -> {
            if (menu.getParentId() == null) {
                roleMenus.add(menu);
            } else {
                RoleMenuDto parent = roleMenuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getSubMenus().add(menu);
                }
            }
        });

        MenuSortableDto.sort(roleMenus);

        return ResponseDto.succeed(roleMenus);
    }

    @Operation(summary = "角色选择列表")
    @RequestMapping(value = "/select/roles", method = RequestMethod.GET)
    public ResponseDto<List<RoleSelectDto>> selectRoles() {
        return ResponseDto.succeed(roleService.getAll(State.enable).stream().map(RoleSelectDto::new).toList());
    }

    @Operation(summary = "菜单选择列表", parameters = {
            @Parameter(name = "type", required = true, description = "菜单类型")
    })
    @RequestMapping(value = "/select/menus", method = RequestMethod.GET)
    public ResponseDto<List<MenuSelectDto>> selectMenus(@NotNull @Integers({1, 2}) Integer type) {
        List<Menu> menus = menuService.getAll(type);

        menus.sort((m1, m2) -> {
            if (type == 1) {
                return m1.getSortBy().compareTo(m2.getSortBy());
            } else {
                if (m1.getParentId().equals(m2.getParentId())) {
                    return m1.getSortBy().compareTo(m2.getSortBy());
                } else {
                    return m1.getParent().getSortBy().compareTo(m2.getParent().getSortBy());
                }
            }
        });

        return ResponseDto.succeed(menus.stream().map(MenuSelectDto::new).toList());
    }

    @Operation(summary = "服务类型选择列表")
    @RequestMapping(value = "/select/server/types", method = RequestMethod.GET)
    public ResponseDto<List<ServerTypeSelectDto>> selectTypes() {
        List<ServerTypeSelectDto> list = serverTypeService.getAll(State.enable).stream().map(ServerTypeSelectDto::new).sorted(Comparator.comparing(ServerTypeSelectDto::getName)).toList();
        return ResponseDto.succeed(list);
    }

    @Operation(summary = "服务主机选择列表")
    @RequestMapping(value = "/select/server/hosts", method = RequestMethod.GET)
    public ResponseDto<List<ServerHostSelectDto>> selectHosts() {
        return ResponseDto.succeed(serverHostService.getAll(State.enable).stream().map(ServerHostSelectDto::new).toList());
    }
}

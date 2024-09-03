package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.entity.Role;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.base.entity.State;
import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.entity.Menu;
import com.sk.ecs.application.base.entity.MenuType;
import com.sk.ecs.application.admin.repository.MenuRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Setter(onMethod_ = @Autowired)
public class MenuService {

    protected static final Set<ObjectId> SYSTEM_MENU_IDS;

    static {
        SYSTEM_MENU_IDS = new HashSet<>();
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000001"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000002"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000003"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000004"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000005"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000006"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000007"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000008"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000009"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000010"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000011"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000012"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000013"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000014"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000015"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000016"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000017"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000018"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000019"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000020"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000021"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000022"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000023"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000024"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000025"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000026"));
        SYSTEM_MENU_IDS.add(new ObjectId("000000000000000000000027"));
    }

    private MenuRepository menuRepository;

    public Menu get(ObjectId id) {
        return menuRepository.findById(id).orElse(null);
    }

    public Menu get(String id) {
        return this.get(new ObjectId(id));
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    public List<Menu> getAll(State state) {
        return menuRepository.findAllByState(state.value());
    }

    public List<Menu> getAll(Integer type) {
        return menuRepository.findAllByType(type);
    }

    public List<Menu> getAll(Role role, @NotNull Set<MenuType> types) {
        if (CollectionUtils.isEmpty(role.getAuthorities())) {
            return new ArrayList<>();
        }

        List<Menu> menus = menuRepository.findAllByIdInAndState(role.getAuthorities(), State.enable.value());
        Set<String> parentIds = menus.stream().filter(menu -> menu.getType() != MenuType.api.value()).map(menu -> menu.getId().toHexString()).collect(Collectors.toSet());
        return menus.stream().filter(menu -> types.contains(MenuType.valueOf(menu.getType())) && (menu.getType() == MenuType.dir.value() || parentIds.contains(menu.getParentId().toHexString()))).toList();
    }

    public Page<Menu> query(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    public Page<Menu> query(Pageable pageable, MenuQueryDto dto) {
        if (dto == null) {
            return query(pageable);
        }
        Menu params = new Menu();
        ExampleMatcher matcher = ExampleMatcher.matching();
        if (dto.getType() != null) {
            params.setType(dto.getType());
        }
        if (dto.getParentName() != null) {
            params.setParent(new Menu());
            params.getParent().setName(dto.getParentName());
            matcher = matcher.withMatcher("parent.name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (dto.getName() != null) {
            params.setName(dto.getName());
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (dto.getPath() != null) {
            params.setPath(dto.getPath());
            matcher = matcher.withMatcher("path", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        return menuRepository.findAll(Example.of(params, matcher), pageable);
    }

    public Menu add(MenuAddDto dto, AdminDetails operator) {
        Menu parent = null;
        ObjectId parentId = null;
        if (dto.getType() != MenuType.dir.value()) {
            if (!StringUtils.hasText(dto.getParentId())) {
                throw new StandardRespondException("上级菜单ID错误");
            }
            parentId = new ObjectId(dto.getParentId());
            parent = this.get(parentId);
            if (parent == null) {
                throw new StandardRespondException("上级菜单不存在");
            }
        }

        MenuType menuType = MenuType.valueOf(dto.getType());
        if (menuType == null) {
            throw new StandardRespondException("菜单类型错误");
        }

        if (menuRepository.existsByIdNotAndParentIdAndName(null, parentId, dto.getName())) {
            throw new StandardRespondException("已存在相同名称的菜单");
        }

        if (menuType != MenuType.dir) {
            if (menuRepository.existsByIdNotAndTypeAndPath(null, menuType.value(), dto.getPath())) {
                throw new StandardRespondException("已存在相同路径的菜单");
            }
        }

        Menu menu = new Menu();
        menu.setType(menuType.value());
        menu.setParentId(parentId);
        menu.setIcon(menuType == MenuType.api ? "" : dto.getIcon());
        menu.setName(dto.getName());
        menu.setPath(menuType == MenuType.dir ? "" : dto.getPath());
        menu.setSortBy(dto.getSortBy());
        menu.setState(State.disable.value());
        menu.initOperation(dto.getRemark(), operator.getUsername());

        menu.setParent(parent);
        menu = menuRepository.save(menu);

        return menu;
    }

    public Menu update(MenuUpdateDto dto, AdminDetails operator) {
        Menu menu = this.get(new ObjectId(dto.getId()));
        if (menu == null) {
            throw new StandardRespondException("ID找不到记录");
        }
        if (dto.getType() != null) {
            MenuType menuType = MenuType.valueOf(dto.getType());
            if (menuType == null) {
                throw new StandardRespondException("菜单类型错误");
            }
            menu.setType(menuType.value());
            if (menuType != MenuType.dir) {
                if (menuRepository.existsByIdNotAndTypeAndPath(menu.getId(), menuType.value(), dto.getPath() == null ? menu.getPath() : dto.getPath())) {
                    throw new StandardRespondException("已存在相同路径的菜单");
                }
            }
        }
        if (dto.getParentId() != null) {
            if (menu.getType() == MenuType.dir.value()) {
                menu.setParentId(null);
                menu.setParent(null);
            } else {
                if (!StringUtils.hasText(dto.getParentId())) {
                    throw new StandardRespondException("上级菜单ID错误");
                }
                if (menuRepository.existsByIdNotAndParentIdAndName(menu.getId(), menu.getParentId(), dto.getName() == null ? menu.getName() : dto.getName())) {
                    throw new StandardRespondException("已存在相同名称的菜单");
                }
                ObjectId parentId = new ObjectId(dto.getParentId());
                Menu parent = this.get(parentId);
                if (parent == null) {
                    throw new StandardRespondException("上级菜单不存在");
                }
                menu.setParentId(parentId);
                menu.setParent(parent);
            }
        }
        if (dto.getIcon() != null) {
            menu.setIcon(menu.getType() == MenuType.api.value() ? "" : dto.getIcon());
            if (menuRepository.existsByIdNotAndParentIdAndName(menu.getId(), menu.getParentId(), menu.getName())) {
                throw new StandardRespondException("已存在相同名称的菜单");
            }
        }
        if (dto.getName() != null) {
            menu.setName(dto.getName());
            if (menuRepository.existsByIdNotAndParentIdAndName(menu.getId(), menu.getParentId(), menu.getName())) {
                throw new StandardRespondException("已存在相同名称的菜单");
            }
        }
        if (dto.getPath() != null) {
            if (menu.getType() == MenuType.dir.value()) {
                menu.setPath("");
            } else {
                menu.setPath(dto.getPath());
                if (menuRepository.existsByIdNotAndTypeAndPath(menu.getId(), menu.getType(), menu.getPath())) {
                    throw new StandardRespondException("已存在相同路径的菜单");
                }
            }
        }
        if (dto.getSortBy() != null) {
            menu.setSortBy(dto.getSortBy());
        }

        menu.updateOperation(dto.getRemark(), operator.getUsername());

        return menuRepository.save(menu);
    }

    public void delete(DeleteDto dto) {
        Set<ObjectId> ids = dto.getIds().stream().map(strId -> {
                    ObjectId id = new ObjectId(strId);
                    if (SYSTEM_MENU_IDS.contains(id)) {
                        throw new StandardRespondException("系统菜单禁止删除");
                    }
                    return id;
                }
        ).collect(Collectors.toSet());

        if (menuRepository.existsAllByParentIdIn(ids)) {
            throw new StandardRespondException("请先删除子菜单");
        }

        menuRepository.deleteAllById(ids);
    }

    public void changeState(ChangeStateDto dto, AdminDetails operator) {
        Set<ObjectId> ids = dto.getIds().stream().map(strId -> {
                    ObjectId id = new ObjectId(strId);
                    if (dto.getState() == State.disable.value()) {
                        if (SYSTEM_MENU_IDS.contains(id)) {
                            throw new StandardRespondException("系统菜单禁止禁用");
                        }
                    }
                    return id;
                }
        ).collect(Collectors.toSet());

        menuRepository.updateState(ids, dto.getState(), operator.getUsername(), LocalDateTime.now());
    }
}

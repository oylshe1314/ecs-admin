package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.base.entity.MenuType;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.admin.entity.Admin;
import com.sk.ecs.application.admin.entity.Menu;
import com.sk.ecs.application.base.entity.State;
import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.auth.RoleApiAuthority;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Setter(onMethod_ = @Autowired)
public class AuthService {

    private PasswordEncoder passwordEncoder;

    private AdminService adminService;
    private MenuService menuService;

    public AdminDetails login(String username, String password) throws AuthenticationException {
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("", new StandardRespondException("用户名或者密码错误"));
        }

        if (admin.getRole().getState() != State.enable.value()) {
            throw new StandardRespondException("角色已禁用");
        }

        if (admin.getState() != State.enable.value()) {
            throw new StandardRespondException("管理员已禁用");
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new BadCredentialsException("", new StandardRespondException("用户名或者密码错误"));
        }

        List<Menu> menus = menuService.getAll(admin.getRole(), Set.of(MenuType.values()));

        return new AdminDetails(admin, menus.stream().map(menu -> new RoleApiAuthority(menu.getPath())).toList());
    }

    public void logout() {

    }
}

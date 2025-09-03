package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.dto.ChangeDetailDto;
import com.sk.ecs.application.admin.dto.ChangePasswordDto;
import com.sk.ecs.application.admin.repository.AdminRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Setter(onMethod_ = @Autowired)
public class SettingService {

    private PasswordEncoder passwordEncoder;

    private AdminService adminService;

    public void changeDetail(ChangeDetailDto dto, AdminDetails adminDetails) {

        adminService.updateDetail(adminDetails.getId(), dto);

        adminDetails.setNickname(dto.getNickname());
        adminDetails.setAvatar(dto.getAvatar());
        adminDetails.setEmail(dto.getEmail());
        adminDetails.setMobile(dto.getMobile());
    }

    public void changePassword(ChangePasswordDto dto, AdminDetails adminDetails) throws Exception {
        if (!passwordEncoder.matches(dto.getOldPassword(), adminDetails.getPassword())) {
            throw new StandardRespondException("旧密码错误");
        }

        String newPassword = passwordEncoder.encode(dto.getNewPassword());

        adminService.updatePassword(adminDetails.getId(), newPassword);

        adminDetails.setPassword(newPassword);
    }
}

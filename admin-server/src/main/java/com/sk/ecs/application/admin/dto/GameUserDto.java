package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sk.ecs.application.admin.feign.base.User;
import com.sk.ecs.application.admin.util.GameChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Schema(title = "游戏账号")
public class GameUserDto {

    @Schema(title = "ID")
    private final String id;

    @Schema(title = "用户ID")
    private final Long userId;

    @Schema(title = "渠道")
    private final Integer channel;

    @Schema(title = "渠道名称")
    private final String channelName;

    @Schema(title = "用户名")
    private final String username;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Schema(title = "创建时间")
    private final LocalDateTime createTime;

    @Schema(title = "最近登录服务器")
    private final String recentServer;

    @Schema(title = "是否封禁")
    private final Boolean BanLogin;

    @Schema(title = "封禁时间")
    private final LocalDateTime BanLoginTime;

    @Schema(title = "第三方信息")
    private final String thirdInfo;

    public GameUserDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.channel = user.getChannel();
        this.channelName = GameChannel.valueOf(this.channel).getName();
        this.username = user.getUsername();
        this.createTime = LocalDateTime.ofEpochSecond(user.getCreateTime(), 0, ZoneOffset.ofHours(8));
        this.recentServer = user.getRecentServer();
        this.BanLogin = Boolean.TRUE.equals(user.getBanLogin());
        if (user.getBanLoginTime() == null || user.getBanLoginTime() == 0) {
            this.BanLoginTime = null;
        } else {
            this.BanLoginTime = LocalDateTime.ofEpochSecond(user.getBanLoginTime(), 0, ZoneOffset.ofHours(8));
        }
        if (user.getThirdInfo() == null) {
            this.thirdInfo = "";
        } else {
            this.thirdInfo = user.getThirdInfo().toString();
        }
    }
}

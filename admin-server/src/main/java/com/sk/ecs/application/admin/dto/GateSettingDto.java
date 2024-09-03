package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.GateSetting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Getter
@Schema(title = "网关设置")
public class GateSettingDto {

    @Schema(title = "最新版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String version;

    @Schema(title = "公告标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String title;

    @Schema(title = "公告内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String content;

    @Schema(title = "是否关闭登录")
    private final Boolean loginClosed;

    @Schema(title = "是否强制下线")
    private final Boolean offline;

    @Schema(title = "是否定时关闭")
    private final Boolean timedClose;

    @Schema(title = "关闭日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate closeDate;

    @Schema(title = "关闭时间")
    @JsonFormat(pattern = "hh:mm:ss")
    private final LocalTime closeTime;

    @Schema(title = "关闭服务器列表")
    private final String closedList;

    @Schema(title = "白名单列表")
    private final String whiteList;

    @JsonCreator
    public GateSettingDto(@JsonProperty("version") String version,
                          @JsonProperty("title") String title,
                          @JsonProperty("content") String content,
                          @JsonProperty("loginClosed") Boolean loginClosed,
                          @JsonProperty("offline") Boolean offline,
                          @JsonProperty("timedClose") Boolean timedClose,
                          @JsonProperty("closeDate") LocalDate closeDate,
                          @JsonProperty("closeTime") LocalTime closeTime,
                          @JsonProperty("closedList") String closedList,
                          @JsonProperty("whiteList") String whiteList) {
        this.version = version;
        this.title = title;
        this.content = content;
        this.loginClosed = loginClosed;
        this.offline = offline;
        this.timedClose = timedClose;
        this.closeDate = closeDate;
        this.closeTime = closeTime;
        this.closedList = closedList;
        this.whiteList = whiteList;
    }

    public GateSettingDto(GateSetting setting) {
        this.version = setting.getVersion();
        this.title = setting.getTitle();
        this.content = setting.getContent();

        this.loginClosed = Boolean.TRUE.equals(setting.getLoginClosed());
        this.offline = loginClosed && Boolean.TRUE.equals(setting.getOffline());
        this.timedClose = loginClosed & Boolean.TRUE.equals(setting.getTimedClose());

        if (!this.timedClose || setting.getCloseTime() == null || setting.getCloseTime() == 0L) {
            this.closeDate = null;
            this.closeTime = null;
        } else {
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(setting.getCloseTime(), 0, ZoneOffset.ofHours(8));
            this.closeDate = dateTime.toLocalDate();
            this.closeTime = dateTime.toLocalTime();
        }

        if (CollectionUtils.isEmpty(setting.getClosedList())) {
            this.closedList = "";
        } else {
            this.closedList = setting.getClosedList().stream().map(Object::toString).collect(Collectors.joining(","));
        }

        if (CollectionUtils.isEmpty(setting.getWhiteList())) {
            this.whiteList = "";
        } else {
            this.whiteList = setting.getWhiteList().stream().map(Object::toString).collect(Collectors.joining(","));
        }
    }
}

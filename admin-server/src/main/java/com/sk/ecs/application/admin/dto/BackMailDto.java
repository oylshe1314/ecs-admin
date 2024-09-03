package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sk.ecs.application.admin.feign.base.BackMail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Schema(title = "后台邮件")
public class BackMailDto {

    @Schema(title = "服务ID")
    private final Integer appId;

    @Schema(title = "邮件ID")
    private final Long id;

    @Schema(title = "标题")
    private final String title;

    @Schema(title = "内容")
    private final String content;

    @Schema(title = "物品ID")
    private final String itemId;

    @Schema(title = "物品数量")
    private final String itemNum;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime createTime;

    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private final LocalDateTime expiration;

    public BackMailDto(Integer appId, Long id, String title, String content, String itemId, String itemNum, LocalDateTime createTime, LocalDateTime expiration) {
        this.appId = appId;
        this.id = id;
        this.title = title;
        this.content = content;
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.createTime = createTime;
        this.expiration = expiration;
    }

    public BackMailDto(Integer appId, BackMail backMail) {
        this.appId = appId;
        this.id = backMail.getId();
        this.title = backMail.getTitle();
        this.content = backMail.getContent();

        if (CollectionUtils.isEmpty(backMail.getItemId())) {
            this.itemId = "";
        } else {
            this.itemId = backMail.getItemId().stream().map(Objects::toString).collect(Collectors.joining(","));
        }

        if (CollectionUtils.isEmpty(backMail.getItemNum())) {
            this.itemNum = "";
        } else {
            this.itemNum = backMail.getItemNum().stream().map(Objects::toString).collect(Collectors.joining(","));
        }

        this.createTime = LocalDateTime.ofEpochSecond(backMail.getCreateTime(), 0, ZoneOffset.ofHours(8));
        this.expiration = LocalDateTime.ofEpochSecond(backMail.getExpiration(), 0, ZoneOffset.ofHours(8));
    }
}

package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.feign.base.Cdkey;
import com.sk.ecs.application.admin.util.GameChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Schema(title = "兑换码")
public class CdkeyDto {

    @Schema(title = "兑换码")
    private final String key;

    @Schema(title = "渠道")
    private final String channel;

    @Schema(title = "有效期")
    private final String expiration;

    @Schema(title = "物品ID")
    private final String itemId;

    @Schema(title = "物品数量")
    private final String itemNum;

    public CdkeyDto(Cdkey cdkey) {
        this.key = cdkey.getKey();
        this.channel = GameChannel.valueOf(cdkey.getChannel()).getName();

        if (cdkey.getExpiration() == null || cdkey.getExpiration() == 0L) {
            this.expiration = "永远有效";
        } else {
            this.expiration = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.ofEpochSecond(cdkey.getExpiration(), 0, ZoneOffset.ofHours(8)));
        }

        if (CollectionUtils.isEmpty(cdkey.getItemId())) {
            this.itemId = "";
        } else {
            this.itemId = cdkey.getItemId().stream().map(Objects::toString).collect(Collectors.joining(","));
        }

        if (CollectionUtils.isEmpty(cdkey.getItemNum())) {
            this.itemNum = "";
        } else {
            this.itemNum = cdkey.getItemNum().stream().map(Objects::toString).collect(Collectors.joining(","));
        }
    }
}

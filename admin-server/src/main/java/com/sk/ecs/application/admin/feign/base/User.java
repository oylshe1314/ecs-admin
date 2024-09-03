package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class User {

    private final String id;

    private final Long userId;

    private final Integer channel;

    private final String username;

    private final Long createTime;

    private final String recentServer;

    private final Boolean banLogin;

    private final Long banLoginTime;

    private final JsonNode thirdInfo;

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("userId") Long userId,
                @JsonProperty("channel") Integer channel,
                @JsonProperty("username") String username,
                @JsonProperty("createTime") Long createTime,
                @JsonProperty("recentServer") String recentServer,
                @JsonProperty("banLogin") Boolean banLogin,
                @JsonProperty("banLoginTime") Long banLoginTime,
                @JsonProperty("thirdInfo") JsonNode thirdInfo) {
        this.id = id;
        this.userId = userId;
        this.channel = channel;
        this.username = username;
        this.createTime = createTime;
        this.recentServer = recentServer;
        this.banLogin = banLogin;
        this.banLoginTime = banLoginTime;
        this.thirdInfo = thirdInfo;
    }
}

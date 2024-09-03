package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ServerReply {

    private final Integer appId;

    private final Integer status;

    private final String message;

    @JsonCreator
    public ServerReply(@JsonProperty("appId") Integer appId,
                       @JsonProperty("status") Integer status,
                       @JsonProperty("message") String message) {
        this.appId = appId;
        this.status = status;
        this.message = message;
    }
}

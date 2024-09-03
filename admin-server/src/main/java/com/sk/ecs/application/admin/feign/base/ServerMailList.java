package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ServerMailList extends ServerReply {

    private final List<BackMail> mailList;

    @JsonCreator
    public ServerMailList(@JsonProperty("appId") Integer appId,
                          @JsonProperty("status") Integer status,
                          @JsonProperty("message") String message,
                          @JsonProperty("mailList") List<BackMail> mailList) {
        super(appId, status, message);
        this.mailList = mailList;
    }
}

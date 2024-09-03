package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class GateSetting {

    private final String version;

    private final String title;

    private final String content;

    private final Boolean loginClosed;

    private final Boolean offline;

    private final Boolean timedClose;

    private final Long closeTime;

    private final List<Integer> closedList;

    private final List<Long> whiteList;

    @JsonCreator
    public GateSetting(@JsonProperty("version") String version,
                       @JsonProperty("title") String title,
                       @JsonProperty("content") String content,
                       @JsonProperty("loginClosed") Boolean loginClosed,
                       @JsonProperty("offline") Boolean offline,
                       @JsonProperty("timedClose") Boolean timedClose,
                       @JsonProperty("closeTime") Long closeTime,
                       @JsonProperty("closedList") List<Integer> closedList,
                       @JsonProperty("whiteList") List<Long> whiteList) {
        this.version = version;
        this.title = title;
        this.content = content;
        this.loginClosed = loginClosed;
        this.offline = offline;
        this.timedClose = timedClose;
        this.closeTime = closeTime;
        this.closedList = closedList;
        this.whiteList = whiteList;
    }
}

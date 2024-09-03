package com.sk.ecs.application.admin.feign.req;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.GateSetting;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateSettingSaveReq extends GateSetting {

    @JsonCreator
    public MsgGateSettingSaveReq(@JsonProperty("version") String version,
                                 @JsonProperty("title") String title,
                                 @JsonProperty("content") String content,
                                 @JsonProperty("loginClosed") Boolean loginClosed,
                                 @JsonProperty("offline") Boolean offline,
                                 @JsonProperty("timedClose") Boolean timedClose,
                                 @JsonProperty("closeTime") Long closeTime,
                                 @JsonProperty("closedList") List<Integer> closedList,
                                 @JsonProperty("whiteList") List<Long> whiteList) {
        super(version, title, content, loginClosed, offline, timedClose, closeTime, closedList, whiteList);
    }
}

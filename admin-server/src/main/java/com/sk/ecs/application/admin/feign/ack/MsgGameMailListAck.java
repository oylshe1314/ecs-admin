package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.ServerMailList;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGameMailListAck {

    private final List<ServerMailList> serverReplies;

    @JsonCreator
    public MsgGameMailListAck(@JsonProperty("serverReplies") List<ServerMailList> serverReplies) {
        this.serverReplies = serverReplies;
    }
}

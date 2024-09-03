package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.ServerReply;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGameMailSendAck {

    private final List<ServerReply> serverReplies;

    public MsgGameMailSendAck(@JsonProperty("serverReplies") List<ServerReply> serverReplies) {
        this.serverReplies = serverReplies;
    }
}

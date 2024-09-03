package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.Cdkey;
import lombok.Getter;

@Getter
public class MsgGateCdkeyGetAck {

    private final Cdkey cdkey;

    @JsonCreator
    public MsgGateCdkeyGetAck(@JsonProperty("cdkey") Cdkey cdkey) {
        this.cdkey = cdkey;
    }
}

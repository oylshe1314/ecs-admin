package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.Cdkey;
import lombok.Getter;

@Getter
public class MsgGateCdkeyAddAck {

    private final Cdkey cdkey;

    @JsonCreator
    public MsgGateCdkeyAddAck(@JsonProperty("cdkey") Cdkey cdkey) {
        this.cdkey = cdkey;
    }
}

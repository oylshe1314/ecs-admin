package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.Cdkey;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateCdkeyGenerateAck {

    private final List<Cdkey> list;

    @JsonCreator
    public MsgGateCdkeyGenerateAck(@JsonProperty("list") List<Cdkey> list) {
        this.list = list;
    }
}

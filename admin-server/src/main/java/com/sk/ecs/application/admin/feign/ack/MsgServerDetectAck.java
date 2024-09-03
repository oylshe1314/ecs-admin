package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.ServerDetectData;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgServerDetectAck {

    private final List<ServerDetectData> list;

    @JsonCreator
    public MsgServerDetectAck(@JsonProperty("list") List<ServerDetectData> list) {
        this.list = list;
    }
}

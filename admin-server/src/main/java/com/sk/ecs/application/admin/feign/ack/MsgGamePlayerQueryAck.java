package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class MsgGamePlayerQueryAck {

    @JsonRawValue
    private final JsonNode player;

    @JsonCreator
    public MsgGamePlayerQueryAck(@JsonProperty("player") JsonNode player) {
        this.player = player;
    }
}

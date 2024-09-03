package com.sk.ecs.application.admin.feign.ack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.admin.feign.base.User;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateUserQueryAck {

    private final Integer total;

    private final List<User> list;

    public MsgGateUserQueryAck(@JsonProperty("total") Integer total, @JsonProperty("list") List<User> list) {
        this.total = total;
        this.list = list;
    }
}

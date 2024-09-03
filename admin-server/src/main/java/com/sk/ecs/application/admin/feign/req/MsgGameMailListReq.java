package com.sk.ecs.application.admin.feign.req;

import lombok.Getter;

@Getter
public class MsgGameMailListReq {

    private final Integer appId;

    public MsgGameMailListReq(Integer appId) {
        this.appId = appId;
    }
}

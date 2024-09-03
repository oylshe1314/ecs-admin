package com.sk.ecs.application.admin.feign.req;

import lombok.Getter;

@Getter
public class MsgGamePlayerOperateReq {

    private final Integer appId;

    private final Long playerId;

    private final Integer operateType;

    private final Integer operateArg;

    public MsgGamePlayerOperateReq(Integer appId, Long playerId, Integer operateType, Integer operateArg) {
        this.appId = appId;
        this.playerId = playerId;
        this.operateType = operateType;
        this.operateArg = operateArg;
    }
}

package com.sk.ecs.application.admin.feign.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MsgGateUserOperateReq {

    private final String userId;

    private final Integer operateType;

    private final String operateArg;

    public MsgGateUserOperateReq(@JsonProperty("userId") String userId,
                                 @JsonProperty("operateType") Integer operateType,
                                 @JsonProperty("operateArg") String operateArg) {
        this.userId = userId;
        this.operateType = operateType;
        this.operateArg = operateArg;
    }
}

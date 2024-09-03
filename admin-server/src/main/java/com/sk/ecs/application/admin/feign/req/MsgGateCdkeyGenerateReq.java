package com.sk.ecs.application.admin.feign.req;

import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateCdkeyGenerateReq {

    private final Integer channel;

    private final Integer count;

    private final Integer validDays;

    private final List<Integer> itemId;

    private final List<Integer> itemNum;

    public MsgGateCdkeyGenerateReq(Integer channel, Integer count, Integer validDays, List<Integer> itemId, List<Integer> itemNum) {
        this.channel = channel;
        this.count = count;
        this.validDays = validDays;
        this.itemId = itemId;
        this.itemNum = itemNum;
    }
}

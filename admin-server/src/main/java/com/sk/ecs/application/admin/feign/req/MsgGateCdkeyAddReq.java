package com.sk.ecs.application.admin.feign.req;

import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateCdkeyAddReq {

    private final Integer channel;

    private final String key;

    private final Integer validDays;

    private final List<Integer> itemId;

    private final List<Integer> itemNum;

    public MsgGateCdkeyAddReq(Integer channel, String key, Integer validDays, List<Integer> itemId, List<Integer> itemNum) {
        this.channel = channel;
        this.key = key;
        this.validDays = validDays;
        this.itemId = itemId;
        this.itemNum = itemNum;
    }
}

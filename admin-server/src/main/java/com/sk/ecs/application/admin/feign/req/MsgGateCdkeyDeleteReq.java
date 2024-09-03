package com.sk.ecs.application.admin.feign.req;

import lombok.Getter;

import java.util.List;

@Getter
public class MsgGateCdkeyDeleteReq {

    private final List<String> keys;

    public MsgGateCdkeyDeleteReq(List<String> keys) {
        this.keys = keys;
    }
}

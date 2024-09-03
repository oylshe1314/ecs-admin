package com.sk.ecs.application.admin.feign.req;

import com.sk.ecs.application.admin.feign.base.ServerDetect;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MsgServerDetectReq {

    private final List<ServerDetect> list;

    public MsgServerDetectReq() {
        this.list = new ArrayList<>();
    }
}

package com.sk.ecs.application.admin.feign.base;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ServerSelectedIds {

    private final Integer appId;

    private final List<Long> ids;

    public ServerSelectedIds(Integer appId, List<Long> ids) {
        this.appId = appId;
        this.ids = ids;
    }

    public ServerSelectedIds(Integer appId) {
        this(appId, new ArrayList<>());
    }
}

package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ServerDetect {

    private final String name;

    private final Integer appId;

    @JsonCreator
    public ServerDetect(String name, Integer appId) {
        this.name = name;
        this.appId = appId;
    }
}

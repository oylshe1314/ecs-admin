package com.sk.ecs.application.admin.dto.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ZookeeperConfigDto {

    private final List<String> servers;

    private final Long timeout;

    @JsonCreator
    public ZookeeperConfigDto(@JsonProperty("servers") List<String> servers,
                              @JsonProperty("timeout") Long timeout) {
        this.servers = servers;
        this.timeout = timeout;
    }
}

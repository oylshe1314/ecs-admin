package com.sk.ecs.application.admin.dto.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class NetworkConfigDto {

    private final String network;

    private final String bind;

    private final String address;

    private final Map<String, Object> extra;

    @JsonCreator
    public NetworkConfigDto(@JsonProperty("network") String network,
                            @JsonProperty("bind") String bind,
                            @JsonProperty("address") String address,
                            @JsonProperty("extra") Map<String, Object> extra) {
        this.network = network;
        this.bind = bind;
        this.address = address;
        this.extra = extra;
    }
}

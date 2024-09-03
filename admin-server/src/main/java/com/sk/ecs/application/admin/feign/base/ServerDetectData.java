package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ServerDetectData extends ServerDetect {

    private final String programHash;

    private final String dataHash;

    private final String configHash;

    private final Integer pid;

    private final Double cpu;

    private final Double memory;

    private final Integer online;

    private final Integer coroutine;

    private final String info;

    @JsonCreator
    public ServerDetectData(@JsonProperty("name") String name,
                            @JsonProperty("appId") Integer appId,
                            @JsonProperty("programHash") String programHash,
                            @JsonProperty("dataHash") String dataHash,
                            @JsonProperty("configHash") String configHash,
                            @JsonProperty("pid") Integer pid,
                            @JsonProperty("cpu") Double cpu,
                            @JsonProperty("memory") Double memory,
                            @JsonProperty("online") Integer online,
                            @JsonProperty("coroutine") Integer coroutine,
                            @JsonProperty("info") String info) {
        super(name, appId);
        this.programHash = programHash;
        this.dataHash = dataHash;
        this.configHash = configHash;
        this.pid = pid;
        this.cpu = cpu;
        this.memory = memory;
        this.online = online;
        this.coroutine = coroutine;
        this.info = info;
    }
}

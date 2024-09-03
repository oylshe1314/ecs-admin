package com.sk.ecs.application.admin.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class ServiceConfigDto {

    private final String name;

    private final Integer appId;

    private final String logDir;

    private final String logLevel;

    private final Boolean logConsole;

    private final Map<String, Object> serverConfig;

    private final NetworkConfigDto innerServer;

    private final NetworkConfigDto exterServer;

    private final DatabaseConfigDto mongoClient;

    private final DatabaseConfigDto mysqlClient;

    private final DatabaseConfigDto redisClient;

    private final ZookeeperConfigDto registerClient;

    private final ZookeeperConfigDto subscribeClient;

    public ServiceConfigDto(@JsonProperty("name") String name,
                            @JsonProperty("appId") Integer appId,
                            @JsonProperty("logDir") String logDir,
                            @JsonProperty("logLevel") String logLevel,
                            @JsonProperty("logConsole") Boolean logConsole,
                            @JsonProperty("serverConfig") Map<String, Object> serverConfig,
                            @JsonProperty("innerServer") NetworkConfigDto innerServer,
                            @JsonProperty("exterServer") NetworkConfigDto exterServer,
                            @JsonProperty("mongoClient") DatabaseConfigDto mongoClient,
                            @JsonProperty("mysqlClient") DatabaseConfigDto mysqlClient,
                            @JsonProperty("redisClient") DatabaseConfigDto redisClient,
                            @JsonProperty("registerClient") ZookeeperConfigDto registerClient,
                            @JsonProperty("subscribeClient") ZookeeperConfigDto subscribeClient) {
        this.name = name;
        this.appId = appId;
        this.logDir = logDir;
        this.logLevel = logLevel;
        this.logConsole = logConsole;
        this.serverConfig = serverConfig;
        this.innerServer = innerServer;
        this.exterServer = exterServer;
        this.mongoClient = mongoClient;
        this.mysqlClient = mysqlClient;
        this.redisClient = redisClient;
        this.registerClient = registerClient;
        this.subscribeClient = subscribeClient;
    }
}

package com.sk.ecs.application.admin.dto.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DatabaseConfigDto {

    private final String address;

    private final String database;

    private final String username;

    private final String password;

    @JsonCreator
    public DatabaseConfigDto(@JsonProperty("address") String address,
                             @JsonProperty("database") String database,
                             @JsonProperty("username") String username,
                             @JsonProperty("password") String password) {
        this.address = address;
        this.database = database;
        this.username = username;
        this.password = password;
    }
}

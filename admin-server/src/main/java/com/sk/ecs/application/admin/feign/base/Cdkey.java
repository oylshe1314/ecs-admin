package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Cdkey {

    private final Integer channel;

    private final String key;

    private final Long expiration;

    private final List<Integer> itemId;

    private final List<Integer> itemNum;

    @JsonCreator
    public Cdkey(@JsonProperty("channel") Integer channel,
                 @JsonProperty("key") String key,
                 @JsonProperty("expiration") Long expiration,
                 @JsonProperty("itemId") List<Integer> itemId,
                 @JsonProperty("itemNum") List<Integer> itemNum) {
        this.channel = channel;
        this.key = key;
        this.expiration = expiration;
        this.itemId = itemId;
        this.itemNum = itemNum;
    }
}

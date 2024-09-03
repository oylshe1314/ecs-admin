package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class BackMail {

    private final Long id;

    private final String title;

    private final String content;

    private final List<Integer> itemId;

    private final List<Integer> itemNum;

    private final Long createTime;

    private final Long expiration;

    @JsonCreator
    public BackMail(@JsonProperty("id") Long id,
                    @JsonProperty("title") String title,
                    @JsonProperty("content") String content,
                    @JsonProperty("itemId") List<Integer> itemId,
                    @JsonProperty("itemNum") List<Integer> itemNum,
                    @JsonProperty("createTime") Long createTime,
                    @JsonProperty("expiration") Long expiration) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.createTime = createTime;
        this.expiration = expiration;
    }
}

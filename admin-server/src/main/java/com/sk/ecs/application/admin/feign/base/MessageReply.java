package com.sk.ecs.application.admin.feign.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageReply<T> {

    private final int status;

    private final String message;

    private final T data;

    @JsonCreator
    public MessageReply(@JsonProperty("status") int status,
                        @JsonProperty("message") String message,
                        @JsonProperty("data") T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

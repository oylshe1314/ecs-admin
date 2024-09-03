package com.sk.ecs.application.admin.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
public class ServerPublishVersion {

    @Id
    private String id;

    private String[] version;

    private LocalDateTime time;
}

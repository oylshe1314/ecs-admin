package com.sk.ecs.application.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "邮件删除")
public class BackMailDeleteDto {

    @NotEmpty
    @Schema(title = "选择列表")
    private final List<BackMailSelectedDto> selectedList;

    @JsonCreator
    public BackMailDeleteDto(@JsonProperty("selectedList") List<BackMailSelectedDto> selectedList) {
        this.selectedList = selectedList;
    }
}

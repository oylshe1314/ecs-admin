package com.sk.ecs.application.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk.ecs.application.common.exception.RespondException;
import com.sk.ecs.application.common.exception.StandardRespondException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "响应公共实体对象")
public class ResponseDto<T> {

    @Schema(name = "status", title = "响应状态", description = "200: 成功")
    private final int status;

    @Schema(name = "message", title = "响应信息")
    private final String message;

    @Schema(name = "data", title = "响应数据")
    private final T data;

    @JsonCreator
    public ResponseDto(@JsonProperty("statue") int statue, @JsonProperty("message") String message) {
        this.status = statue;
        this.message = message;
        this.data = null;
    }

    @JsonCreator
    public ResponseDto(@JsonProperty("statue") int statue, @JsonProperty("message") String message, @JsonProperty("data") T data) {
        this.status = statue;
        this.message = message;
        this.data = data;
    }

    public boolean success() {
        return status == ResponseStatus.SUCCESSFUL.getStatus();
    }

    public boolean failed() {
        return !success();
    }

    public RuntimeException cause() {
        return new StandardRespondException(this.status, this.message);
    }

    public static ResponseDto<?> succeed() {
        return succeed(null);
    }

    public static <T> ResponseDto<T> succeed(T data) {
        return new ResponseDto<>(ResponseStatus.SUCCESSFUL.getStatus(), ResponseStatus.SUCCESSFUL.getMessage(), data);
    }

    public static ResponseDto<?> fail(int status, String message) {
        return fail(status, message, null);
    }

    public static <T> ResponseDto<T> fail(int status, String message, T data) {
        return new ResponseDto<>(status, message, data);
    }

    public static ResponseDto<?> fail(ResponseStatus status) {
        return fail(status.getStatus(), status.getMessage(), null);
    }

    public static <T> ResponseDto<T> fail(ResponseStatus status, T data) {
        return fail(status.getStatus(), status.getMessage(), data);
    }

    public static ResponseDto<?> fail(ResponseStatus status, String message) {
        return fail(status.getStatus(), message, null);
    }

    public static <T> ResponseDto<T> fail(ResponseStatus status, String message, T data) {
        return fail(status.getStatus(), message, data);
    }

    public static ResponseDto<?> fail(String message) {
        return fail(message, null);
    }

    public static <T> ResponseDto<T> fail(String message, T data) {
        return fail(ResponseStatus.FAILED.getStatus(), message, data);
    }

    public static ResponseDto<?> fail(Throwable cause) {
        return fail(cause, null);
    }

    public static <T> ResponseDto<T> fail(Throwable cause, T data) {
        RespondException respondException = RespondException.extractRespondException(cause);
        if (respondException != null) {
            return fail(respondException.getStatus(), respondException.getMessage(), data);
        } else {
            return fail(ResponseStatus.UNKNOWN.getStatus(), ResponseStatus.UNKNOWN.getMessage(), data);
        }
    }
}

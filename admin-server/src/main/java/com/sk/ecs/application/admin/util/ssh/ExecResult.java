package com.sk.ecs.application.admin.util.ssh;

import lombok.Getter;

@Getter
public class ExecResult {

    private final int status;

    private final String output;

    private final String error;

    public ExecResult(Integer status, String output, String error) {
        this.status = status == null ? 0 : status;
        this.output = output;
        this.error = error;
    }

    public boolean isSuccess() {
        return status == 0;
    }
}

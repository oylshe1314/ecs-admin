package com.sk.ecs.application.admin.feign.req;

import com.sk.ecs.application.admin.feign.base.BackMail;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGameMailSendReq {

    private final List<Integer> appIds;

    private final List<Long> playerIds;

    private final BackMail backMail;

    public MsgGameMailSendReq(List<Integer> appIds, List<Long> playerIds, BackMail backMail) {
        this.appIds = appIds;
        this.playerIds = playerIds;
        this.backMail = backMail;
    }
}

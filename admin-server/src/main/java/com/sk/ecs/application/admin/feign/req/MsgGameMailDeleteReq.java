package com.sk.ecs.application.admin.feign.req;

import com.sk.ecs.application.admin.feign.base.ServerSelectedIds;
import lombok.Getter;

import java.util.List;

@Getter
public class MsgGameMailDeleteReq {

    private final List<ServerSelectedIds> SelectedLists;

    public MsgGameMailDeleteReq(List<ServerSelectedIds> selectedLists) {
        SelectedLists = selectedLists;
    }
}

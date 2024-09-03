package com.sk.ecs.application.admin.feign;

import com.sk.ecs.application.admin.feign.ack.*;
import com.sk.ecs.application.admin.feign.base.MessageReply;
import com.sk.ecs.application.admin.feign.req.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ecsAdminClient", url = "${ecs.admin.url}")
public interface EcsAdminClient {

    @RequestMapping(value = "/server/detect", method = RequestMethod.POST)
    MessageReply<MsgServerDetectAck> serverDetect(MsgServerDetectReq req) throws Exception;

    @RequestMapping(value = "/server/reload", method = RequestMethod.POST)
    MessageReply<?> serverReload() throws Exception;

    @RequestMapping(value = "/gate/setting/get", method = RequestMethod.GET)
    MessageReply<MsgGateSettingGetAck> getGateSetting() throws Exception;

    @RequestMapping(value = "/gate/setting/save", method = RequestMethod.POST)
    MessageReply<?> saveGateSetting(MsgGateSettingSaveReq req) throws Exception;

    @RequestMapping(value = "/gate/user/query", method = RequestMethod.GET)
    MessageReply<MsgGateUserQueryAck> gateUserQuery(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "channel", required = false) Integer channel, @RequestParam(value = "userId", required = false) Long userId) throws Exception;

    @RequestMapping(value = "/gate/user/operate", method = RequestMethod.POST)
    MessageReply<MsgGateUserOperateAck> gateUserOperate(@RequestBody MsgGateUserOperateReq req) throws Exception;

    @RequestMapping(value = "/gate/cdkey/get", method = RequestMethod.GET)
    MessageReply<MsgGateCdkeyGetAck> gateCdkeyGet(@RequestParam("key") String key) throws Exception;

    @RequestMapping(value = "/gate/cdkey/list", method = RequestMethod.GET)
    MessageReply<MsgGateCdkeyListAck> gateCdkeyList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "channel", required = false) Integer channel) throws Exception;

    @RequestMapping(value = "/gate/cdkey/add", method = RequestMethod.POST)
    MessageReply<MsgGateCdkeyAddAck> gateCdkeyAdd(@RequestBody MsgGateCdkeyAddReq req) throws Exception;

    @RequestMapping(value = "/gate/cdkey/generate", method = RequestMethod.POST)
    MessageReply<MsgGateCdkeyGenerateAck> gateCdkeyGenerate(@RequestBody MsgGateCdkeyGenerateReq req) throws Exception;

    @RequestMapping(value = "/gate/cdkey/delete", method = RequestMethod.POST)
    MessageReply<MsgGateCdkeyDeleteAck> gateCdkeyDelete(@RequestBody MsgGateCdkeyDeleteReq req) throws Exception;

    @RequestMapping(value = "/game/mail/list", method = RequestMethod.GET)
    MessageReply<MsgGameMailListAck> gameMailList(@RequestParam("appId") Integer appId) throws Exception;

    @RequestMapping(value = "/game/mail/send", method = RequestMethod.POST)
    MessageReply<MsgGameMailSendAck> gameMailSend(@RequestBody MsgGameMailSendReq req) throws Exception;

    @RequestMapping(value = "/game/mail/delete", method = RequestMethod.POST)
    MessageReply<MsgGameMailDeleteAck> gameMailDelete(@RequestBody MsgGameMailDeleteReq req) throws Exception;

    @RequestMapping(value = "/game/player/query", method = RequestMethod.GET)
    MessageReply<MsgGamePlayerQueryAck> gamePlayerQuery(@RequestParam("appId") Integer appId, @RequestParam("playerId") Long playerId);

    @RequestMapping(value = "/game/player/operate", method = RequestMethod.POST)
    MessageReply<MsgGamePlayerOperateAck> gamePlayerOperate(@RequestBody MsgGamePlayerOperateReq req);
}

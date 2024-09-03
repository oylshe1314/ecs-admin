package com.sk.ecs.application.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.feign.EcsAdminClient;
import com.sk.ecs.application.admin.feign.ack.*;
import com.sk.ecs.application.admin.feign.base.*;
import com.sk.ecs.application.admin.feign.req.*;
import com.sk.ecs.application.common.dto.PageResultDto;
import com.sk.ecs.application.common.dto.ResponseDto;
import com.sk.ecs.application.common.exception.StandardRespondException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@Setter(onMethod_ = @Autowired)
public class GameService {

    private EcsAdminClient ecsAdminClient;

    public List<BackMailDto> mailList(Integer appId) throws Exception {
        if (appId == null) {
            appId = 0;
        }
        MessageReply<MsgGameMailListAck> reply = ecsAdminClient.gameMailList(appId);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("获取邮件列表失败: " + reply.getMessage());
        }

        MsgGameMailListAck ack = reply.getData();

        List<BackMailDto> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ack.getServerReplies())) {
            for (ServerMailList serverReply : ack.getServerReplies()) {
                if (serverReply.getStatus() != 0) {
                    log.error(String.format("邮件列表获取失败, appId: %d, message: %s", serverReply.getAppId(), serverReply.getMailList()));
                    continue;
                }

                if (!CollectionUtils.isEmpty(serverReply.getMailList())) {
                    for (BackMail backMail : serverReply.getMailList()) {
                        list.add(new BackMailDto(serverReply.getAppId(), backMail));
                    }
                }
            }
        }
        return list;
    }

    public void mailSend(BackMailSendDto dto) throws Exception {
        long now = System.currentTimeMillis() / 1000;
        long exp = dto.getExpiration() == 0 ? 0L : now + 86400L * dto.getExpiration();

        MsgGameMailSendReq req = new MsgGameMailSendReq(dto.getAppIds(), dto.getPlayerIds(), new BackMail(0L, dto.getTitle(), dto.getContent(), dto.getItemId(), dto.getItemNum(), now, exp));

        MessageReply<MsgGameMailSendAck> reply = ecsAdminClient.gameMailSend(req);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("邮件发送失败：" + reply.getMessage());
        }

        MsgGameMailSendAck ack = reply.getData();

        List<ServerReply> serverReplies = new ArrayList<>();
        for (ServerReply serverReply : ack.getServerReplies()) {
            if (serverReply.getStatus() != 0) {
                serverReplies.add(serverReply);
            }
        }

        if (!serverReplies.isEmpty()) {
            List<String> messages = new ArrayList<>();
            for (ServerReply serverReply : serverReplies) {
                messages.add(String.format("AppId: %d, Message: %s", serverReply.getAppId(), serverReply.getMessage()));
            }
            log.error("发送邮件时部分服务器返回错误：" + String.join(",", messages));
            throw new StandardRespondException("发送邮件部分服务器返回错误");
        }
    }

    public void mailDelete(BackMailDeleteDto dto) throws Exception {
        Map<Integer, ServerSelectedIds> serverMap = new HashMap<>();
        for (BackMailSelectedDto selectedDto : dto.getSelectedList()) {
            ServerSelectedIds selectedIds = serverMap.computeIfAbsent(selectedDto.getAppId(), ServerSelectedIds::new);
            selectedIds.getIds().add(selectedDto.getId());
        }

        MessageReply<MsgGameMailDeleteAck> reply = ecsAdminClient.gameMailDelete(new MsgGameMailDeleteReq(new ArrayList<>(serverMap.values())));
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("邮件发送失败：" + reply.getMessage());
        }

        MsgGameMailDeleteAck ack = reply.getData();

        List<ServerReply> serverReplies = new ArrayList<>();
        for (ServerReply serverReply : ack.getServerReplies()) {
            if (serverReply.getStatus() != 0) {
                serverReplies.add(serverReply);
            }
        }

        if (!serverReplies.isEmpty()) {
            List<String> messages = new ArrayList<>();
            for (ServerReply serverReply : serverReplies) {
                messages.add(String.format("AppId: %d, Message: %s", serverReply.getAppId(), serverReply.getMessage()));
            }
            log.error("删除邮件时部分服务器返回错误：" + String.join(",", messages));
            throw new StandardRespondException("删除邮件部分服务器返回错误");
        }
    }

    public PageResultDto<CdkeyDto> cdkeyQuery(Pageable pageable, CdkeyQueryDto dto) throws Exception {
        Page<Cdkey> page;
        if (dto != null && StringUtils.hasText(dto.getKey())) {
            MessageReply<MsgGateCdkeyGetAck> reply = this.ecsAdminClient.gateCdkeyGet(dto.getKey());
            if (reply.getStatus() != 0) {
                throw new StandardRespondException("兑换码查询失败: " + reply.getMessage());
            }

            MsgGateCdkeyGetAck ack = reply.getData();

            if (ack.getCdkey() == null) {
                page = new PageImpl<>(new ArrayList<>(), pageable, 0);
            } else {
                page = new PageImpl<>(List.of(ack.getCdkey()), pageable, 1);
            }
        } else {
            Integer channel = null;
            if (dto != null) {
                channel = dto.getChannel();
            }
            MessageReply<MsgGateCdkeyListAck> reply = this.ecsAdminClient.gateCdkeyList(pageable.getPageNumber() + 1, pageable.getPageSize(), channel);
            if (reply.getStatus() != 0) {
                throw new StandardRespondException("兑换码查询失败: " + reply.getMessage());
            }

            MsgGateCdkeyListAck ack = reply.getData();

            page = new PageImpl<>(ack.getList(), pageable, ack.getTotal());
        }
        return new PageResultDto<>(pageable, page, CdkeyDto::new);
    }

    public void cdkeyAdd(CdkeyAddDto dto) throws Exception {
        MsgGateCdkeyAddReq req = new MsgGateCdkeyAddReq(dto.getChannel(), dto.getKey(), dto.getValidDays(), dto.getItemId(), dto.getItemNum());

        MessageReply<MsgGateCdkeyAddAck> reply = ecsAdminClient.gateCdkeyAdd(req);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("兑换码添加失败: " + reply.getMessage());
        }
    }

    public void cdkeyGenerate(CdkeyGenerateDto dto) throws Exception {
        MsgGateCdkeyGenerateReq req = new MsgGateCdkeyGenerateReq(dto.getChannel(), dto.getCount(), dto.getValidDays(), dto.getItemId(), dto.getItemNum());

        MessageReply<MsgGateCdkeyGenerateAck> reply = ecsAdminClient.gateCdkeyGenerate(req);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("兑换码生成失败: " + reply.getMessage());
        }
    }

    public void cdkeyDelete(DeleteDto dto) throws Exception {
        MsgGateCdkeyDeleteReq req = new MsgGateCdkeyDeleteReq(dto.getIds());

        MessageReply<MsgGateCdkeyDeleteAck> reply = ecsAdminClient.gateCdkeyDelete(req);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("兑换码删除失败: " + reply.getMessage());
        }
    }

    public PageResultDto<GameUserDto> userQuery(Pageable pageable, GameUserQueryDto dto) throws Exception {
        Integer channel = null;
        Long userId = null;
        if (dto != null) {
            channel = dto.getChannel();
            userId = dto.getUserId();
        }

        MessageReply<MsgGateUserQueryAck> reply = ecsAdminClient.gateUserQuery(pageable.getPageNumber() + 1, pageable.getPageSize(), channel, userId);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("账号查询失败: " + reply.getMessage());
        }

        MsgGateUserQueryAck ack = reply.getData();

        return new PageResultDto<>(pageable, new PageImpl<>(ack.getList(), pageable, ack.getTotal()), GameUserDto::new);
    }

    public void userOperate(GameUserOperateDto dto) throws Exception {
        MessageReply<MsgGateUserOperateAck> reply = ecsAdminClient.gateUserOperate(new MsgGateUserOperateReq(dto.getUserId(), dto.getOperate(), dto.getArgs()));
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("账号操作失败: " + reply.getMessage());
        }
    }

    public GamePlayerDto playerQuery(Integer appId, Long playerId) throws Exception {
        MessageReply<MsgGamePlayerQueryAck> reply = ecsAdminClient.gamePlayerQuery(appId, playerId);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("玩家查询失败: " + reply.getMessage());
        }

        MsgGamePlayerQueryAck ack = reply.getData();

        JsonNode player = ack.getPlayer();

        return new GamePlayerDto(player == null ? null : ack.getPlayer().toString());
    }

    public void playerOperate(GamePlayerOperateDto dto) throws Exception {
        MessageReply<MsgGamePlayerOperateAck> reply = ecsAdminClient.gamePlayerOperate(new MsgGamePlayerOperateReq(dto.getAppId(), dto.getPlayerId(), dto.getOperate(), dto.getArgs()));
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("玩家操作失败: " + reply.getMessage());
        }
    }
}

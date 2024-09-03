package com.sk.ecs.application.admin.controller;

import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.service.GameService;
import com.sk.ecs.application.common.dto.PageRequestDto;
import com.sk.ecs.application.common.dto.PageResultDto;
import com.sk.ecs.application.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/game")
@Tag(name = "game", description = "游戏相关接口")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "邮件列表", parameters = {
            @Parameter(name = "appId", description = "服务ID")
    })
    @RequestMapping(value = "/mail/list", method = RequestMethod.GET)
    public ResponseDto<List<BackMailDto>> mailList(@Positive Integer appId) throws Exception {
        return ResponseDto.succeed(gameService.mailList(appId));
    }

    @Operation(summary = "邮件发送")
    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    public ResponseDto<?> mailSend(@RequestBody @Valid BackMailSendDto dto) throws Exception {
        gameService.mailSend(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "邮件删除")
    @RequestMapping(value = "/mail/delete", method = RequestMethod.POST)
    public ResponseDto<?> mailDelete(@RequestBody @Valid BackMailDeleteDto dto) throws Exception {
        gameService.mailDelete(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "兑换码查询")
    @RequestMapping(value = "/cdkey/query", method = RequestMethod.GET)
    public ResponseDto<PageResultDto<CdkeyDto>> cdkeyQuery(@NotNull @Positive Integer pageNo, @NotNull @Range(min = 10, max = 50) Integer pageSize, @Parameter(hidden = true) @Valid CdkeyQueryDto dto) throws Exception {
        if (dto != null) {
            if (dto.getChannel() == null && dto.getKey() == null) {
                dto = null;
            }
        }
        return ResponseDto.succeed(gameService.cdkeyQuery(PageRequest.of(pageNo - 1, pageSize), dto));
    }

    @Operation(summary = "兑换码查询")
    @RequestMapping(value = "/cdkey/query", method = RequestMethod.POST)
    public ResponseDto<PageResultDto<CdkeyDto>> cdkeyQuery(@RequestBody @Valid PageRequestDto<CdkeyQueryDto> dto) throws Exception {
        return this.cdkeyQuery(dto.getPageNo(), dto.getPageSize(), dto.getQuery());
    }

    @Operation(summary = "兑换码添加")
    @RequestMapping(value = "/cdkey/add", method = RequestMethod.POST)
    public ResponseDto<?> cdkeyAdd(@RequestBody @Valid CdkeyAddDto dto) throws Exception {
        gameService.cdkeyAdd(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "兑换码生成")
    @RequestMapping(value = "/cdkey/generate", method = RequestMethod.POST)
    public ResponseDto<?> cdkeyGenerate(@RequestBody @Valid CdkeyGenerateDto dto) throws Exception {
        gameService.cdkeyGenerate(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "兑换码生成")
    @RequestMapping(value = "/cdkey/delete", method = RequestMethod.POST)
    public ResponseDto<?> cdkeyDelete(@RequestBody @Valid DeleteDto dto) throws Exception {
        gameService.cdkeyDelete(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "账号查询")
    @RequestMapping(value = "/user/query", method = RequestMethod.GET)
    public ResponseDto<PageResultDto<GameUserDto>> userQuery(@NotNull @Positive Integer pageNo, @NotNull @Range(min = 10, max = 50) Integer pageSize, @Parameter(hidden = true) @Valid GameUserQueryDto dto) throws Exception {
        if (dto != null) {
            if (dto.getChannel() == null && dto.getUserId() == null) {
                dto = null;
            }
        }
        return ResponseDto.succeed(gameService.userQuery(PageRequest.of(pageNo - 1, pageSize), dto));
    }

    @Operation(summary = "账号查询")
    @RequestMapping(value = "/user/query", method = RequestMethod.POST)
    public ResponseDto<PageResultDto<GameUserDto>> userQuery(@RequestBody @Valid PageRequestDto<GameUserQueryDto> dto) throws Exception {
        return this.userQuery(dto.getPageNo(), dto.getPageSize(), dto.getQuery());
    }

    @Operation(summary = "账号操作")
    @RequestMapping(value = "/user/operate", method = RequestMethod.POST)
    public ResponseDto<?> userOperate(@RequestBody @Valid GameUserOperateDto dto) throws Exception {
        gameService.userOperate(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "玩家查询")
    @RequestMapping(value = "/player/query", method = RequestMethod.GET)
    public ResponseDto<GamePlayerDto> playerQuery(@NotNull @Positive Integer appId, @NotNull @Positive Long playerId) throws Exception {
        return ResponseDto.succeed(gameService.playerQuery(appId, playerId));
    }

    @Operation(summary = "玩家操作")
    @RequestMapping(value = "/player/operate", method = RequestMethod.POST)
    public ResponseDto<?> playerOperate(@RequestBody @Valid GamePlayerOperateDto dto) throws Exception {
        gameService.playerOperate(dto);
        return ResponseDto.succeed();
    }
}

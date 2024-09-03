package com.sk.ecs.application.admin.controller;


import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.entity.ServerConfig;
import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerType;
import com.sk.ecs.application.admin.service.ServerConfigService;
import com.sk.ecs.application.admin.service.ServerHostService;
import com.sk.ecs.application.admin.service.ServerService;
import com.sk.ecs.application.admin.service.ServerTypeService;
import com.sk.ecs.application.common.dto.PageRequestDto;
import com.sk.ecs.application.common.dto.PageResultDto;
import com.sk.ecs.application.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/server")
@Tag(name = "server", description = "服务相关接口")
public class ServerController {

    private final ServerTypeService serverTypeService;
    private final ServerHostService serverHostService;
    private final ServerConfigService serverConfigService;
    private final ServerService serverService;

    public ServerController(ServerTypeService serverTypeService, ServerHostService serverHostService, ServerConfigService serverConfigService, ServerService serverService) {
        this.serverTypeService = serverTypeService;
        this.serverHostService = serverHostService;
        this.serverConfigService = serverConfigService;
        this.serverService = serverService;
    }

    @Operation(summary = "服务类型查询", parameters = {
            @Parameter(name = "pageNo", required = true, description = "页码(从1开始)"),
            @Parameter(name = "pageSize", required = true, description = "单页数目(10-50)"),
            @Parameter(name = "name", description = "查询参数 - 主机名称", schema = @Schema(implementation = String.class)),
            @Parameter(name = "host", description = "查询参数 - 主机地址", schema = @Schema(implementation = String.class)),
    })
    @RequestMapping(value = "/type/query", method = RequestMethod.GET)
    public ResponseDto<PageResultDto<ServerTypeDto>> typeQuery(@NotNull @Positive Integer pageNo, @NotNull @Range(min = 10, max = 50) Integer pageSize, @Parameter(hidden = true) @Valid ServerTypeQueryDto dto) throws Exception {
        if (dto != null && dto.getName() == null) {
            dto = null;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ServerType> page = serverTypeService.query(pageable, dto);
        return ResponseDto.succeed(new PageResultDto<>(pageable, page, ServerTypeDto::new));
    }

    @Operation(summary = "服务类型查询")
    @RequestMapping(value = "/type/query", method = RequestMethod.POST)
    public ResponseDto<PageResultDto<ServerTypeDto>> typeQuery(@RequestBody @Valid PageRequestDto<ServerTypeQueryDto> dto) throws Exception {
        return typeQuery(dto.getPageNo(), dto.getPageSize(), dto.getQuery());
    }

    @Operation(summary = "服务类型添加")
    @RequestMapping(value = "/type/add", method = RequestMethod.POST)
    public ResponseDto<ServerTypeDto> typeAdd(@RequestBody @Valid ServerTypeAddDto dto, Authentication authentication) {
        return ResponseDto.succeed(new ServerTypeDto(serverTypeService.add(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务类型修改")
    @RequestMapping(value = "/type/update", method = RequestMethod.POST)
    public ResponseDto<ServerTypeDto> typeUpdate(@RequestBody @Valid ServerTypeUpdateDto dto, Authentication authentication) {
        return ResponseDto.succeed(new ServerTypeDto(serverTypeService.update(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务类型删除")
    @RequestMapping(value = "/type/delete", method = RequestMethod.POST)
    public ResponseDto<?> typeDelete(@RequestBody @Valid DeleteDto dto) {
        serverTypeService.delete(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务类型状态修改")
    @RequestMapping(value = "/type/change/state", method = RequestMethod.POST)
    public ResponseDto<?> typeChangeState(@RequestBody @Valid ChangeStateDto dto, Authentication authentication) {
        serverTypeService.changeState(dto, (AdminDetails) authentication.getPrincipal());
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务包上传")
    @RequestMapping(value = "/package/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<?> packageUpload(@Valid ServerPackageUploadDto dto) throws Exception {
        serverTypeService.upload(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务主机查询", parameters = {
            @Parameter(name = "pageNo", required = true, description = "页码(从1开始)"),
            @Parameter(name = "pageSize", required = true, description = "单页数目(10-50)"),
            @Parameter(name = "name", description = "查询参数 - 主机名称", schema = @Schema(implementation = String.class)),
            @Parameter(name = "host", description = "查询参数 - 主机地址", schema = @Schema(implementation = String.class)),
    })
    @RequestMapping(value = "/host/query", method = RequestMethod.GET)
    public ResponseDto<PageResultDto<ServerHostDto>> hostQuery(@NotNull @Positive Integer pageNo, @NotNull @Range(min = 10, max = 50) Integer pageSize, @Parameter(hidden = true) @Valid ServerHostQueryDto dto) throws Exception {
        if (dto != null && dto.getName() == null && dto.getHost() == null) {
            dto = null;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ServerHost> page = serverHostService.query(pageable, dto);
        return ResponseDto.succeed(new PageResultDto<>(pageable, page, ServerHostDto::new));
    }

    @Operation(summary = "服务主机查询")
    @RequestMapping(value = "/host/query", method = RequestMethod.POST)
    public ResponseDto<PageResultDto<ServerHostDto>> hostQuery(@RequestBody @Valid PageRequestDto<ServerHostQueryDto> dto) throws Exception {
        return hostQuery(dto.getPageNo(), dto.getPageSize(), dto.getQuery());
    }

    @Operation(summary = "服务主机添加")
    @RequestMapping(value = "/host/add", method = RequestMethod.POST)
    public ResponseDto<ServerHostDto> hostAdd(@RequestBody @Valid ServerHostAddDto dto, Authentication authentication) {
        return ResponseDto.succeed(new ServerHostDto(serverHostService.add(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务主机修改")
    @RequestMapping(value = "/host/update", method = RequestMethod.POST)
    public ResponseDto<ServerHostDto> hostUpdate(@RequestBody @Valid ServerHostUpdateDto dto, Authentication authentication) {
        return ResponseDto.succeed(new ServerHostDto(serverHostService.update(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务主机删除")
    @RequestMapping(value = "/host/delete", method = RequestMethod.POST)
    public ResponseDto<?> hostDelete(@RequestBody @Valid DeleteDto dto) {
        serverHostService.delete(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务主机状态修改")
    @RequestMapping(value = "/host/change/state", method = RequestMethod.POST)
    public ResponseDto<?> hostChangeState(@RequestBody @Valid ChangeStateDto dto, Authentication authentication) {
        serverHostService.changeState(dto, (AdminDetails) authentication.getPrincipal());
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务配置查询", parameters = {
            @Parameter(name = "pageNo", required = true, description = "页码(从1开始)"),
            @Parameter(name = "pageSize", required = true, description = "单页数目(10-50)"),
            @Parameter(name = "typeId", description = "查询参数 - 服务类型", schema = @Schema(implementation = String.class)),
            @Parameter(name = "appId", description = "查询参数 - 服务ID", schema = @Schema(implementation = Integer.class)),
    })
    @RequestMapping(value = "/config/query", method = RequestMethod.GET)
    public ResponseDto<PageResultDto<ServerConfigDto>> configQuery(@NotNull @Positive Integer pageNo, @NotNull @Range(min = 10, max = 50) Integer pageSize, @Parameter(hidden = true) @Valid ServerConfigQueryDto dto) throws Exception {
        if (dto != null && dto.getTypeId() == null && dto.getAppId() == null) {
            dto = null;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ServerConfig> page = serverConfigService.query(pageable, dto);
        return ResponseDto.succeed(new PageResultDto<>(pageable, page, serverConfigService::toServerConfigDto));
    }

    @Operation(summary = "服务配置查询")
    @RequestMapping(value = "/config/query", method = RequestMethod.POST)
    public ResponseDto<PageResultDto<ServerConfigDto>> configQuery(@RequestBody @Valid PageRequestDto<ServerConfigQueryDto> dto) throws Exception {
        return configQuery(dto.getPageNo(), dto.getPageSize(), dto.getQuery());
    }

    @Operation(summary = "服务配置添加")
    @RequestMapping(value = "/config/add", method = RequestMethod.POST)
    public ResponseDto<ServerConfigDto> configAdd(@RequestBody @Valid ServerConfigAddDto dto, Authentication authentication) throws Exception {
        return ResponseDto.succeed(serverConfigService.toServerConfigDto(serverConfigService.add(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务配置修改")
    @RequestMapping(value = "/config/update", method = RequestMethod.POST)
    public ResponseDto<ServerConfigDto> configUpdate(@RequestBody @Valid ServerConfigUpdateDto dto, Authentication authentication) throws Exception {
        return ResponseDto.succeed(serverConfigService.toServerConfigDto(serverConfigService.update(dto, (AdminDetails) authentication.getPrincipal())));
    }

    @Operation(summary = "服务配置删除")
    @RequestMapping(value = "/config/delete", method = RequestMethod.POST)
    public ResponseDto<?> configDelete(@RequestBody @Valid DeleteDto dto) {
        serverConfigService.delete(dto);
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务配置状态修改")
    @RequestMapping(value = "/config/change/state", method = RequestMethod.POST)
    public ResponseDto<?> configChangeState(@RequestBody @Valid ChangeStateDto dto, Authentication authentication) {
        serverConfigService.changeState(dto, (AdminDetails) authentication.getPrincipal());
        return ResponseDto.succeed();
    }

    @Operation(summary = "服务列表", parameters = {
            @Parameter(name = "typeId", description = "查询参数 - 服务类型", schema = @Schema(implementation = String.class)),
            @Parameter(name = "appId", description = "查询参数 - 服务ID", schema = @Schema(implementation = Integer.class)),
    })
    @RequestMapping(value = "/service/list", method = RequestMethod.GET)
    public ResponseDto<List<ServiceDto>> serviceList(@Parameter(hidden = true) @Valid ServerConfigQueryDto dto) throws Exception {
        return ResponseDto.succeed(serverService.getList(dto));
    }

    @Operation(summary = "服务列表", parameters = {
            @Parameter(name = "typeId", description = "查询参数 - 服务类型", schema = @Schema(implementation = String.class)),
            @Parameter(name = "appId", description = "查询参数 - 服务ID", schema = @Schema(implementation = Integer.class)),
    })
    @RequestMapping(value = "/service/query", method = RequestMethod.GET)
    public ResponseDto<List<ServiceDto>> serviceQuery(@Parameter(hidden = true) @Valid ServerConfigQueryDto dto) throws Exception {
        return ResponseDto.succeed(serverService.query(dto));
    }

    @Operation(summary = "服务诊断",parameters = {
            @Parameter(name = "configId", description = "查询参数 - 配置ID", schema = @Schema(implementation = String.class)),
    })
    @RequestMapping(value = "/service/detect", method = RequestMethod.GET)
    public ResponseDto<ServiceDetectDto> serviceDetect(@NotEmpty String configId) throws Exception {
        return ResponseDto.succeed(serverService.serviceDetect(configId));
    }

    @Operation(summary = "服务发布")
    @RequestMapping(value = "/service/publish", method = RequestMethod.POST)
    public ResponseDto<?> servicePublish(@RequestBody @Valid ServicePublishDto dto) throws Exception {
        return ResponseDto.succeed(serverService.publish(dto));
    }

    @Operation(summary = "服务控制")
    @RequestMapping(value = "/service/control", method = RequestMethod.POST)
    public ResponseDto<ServiceDetectDto> serviceControl(@RequestBody @Valid ServiceControlDto dto) throws Exception {
        return ResponseDto.succeed(serverService.control(dto));
    }

    @Operation(summary = "网关配置获取")
    @RequestMapping(value = "/gate/setting/get", method = RequestMethod.GET)
    public ResponseDto<GateSettingDto> getGateSetting() throws Exception {
        return ResponseDto.succeed(serverService.getGateSetting());
    }

    @Operation(summary = "网关配置保存")
    @RequestMapping(value = "/gate/setting/save", method = RequestMethod.POST)
    public ResponseDto<GateSettingDto> saveGateSetting(@RequestBody @Valid GateSettingDto dto) throws Exception {
        return ResponseDto.succeed(serverService.saveGateSetting(dto));
    }
}

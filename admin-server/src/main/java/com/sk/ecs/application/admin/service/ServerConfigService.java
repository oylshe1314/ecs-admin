package com.sk.ecs.application.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.dto.config.ServiceConfigDto;
import com.sk.ecs.application.admin.entity.ServerConfig;
import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerType;
import com.sk.ecs.application.admin.repository.ServerConfigRepository;
import com.sk.ecs.application.common.dto.PageResultDto;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.base.entity.State;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Setter(onMethod_ = @Autowired)
public class ServerConfigService {

    private ServerTypeService serverTypeService;

    private ServerHostService serverHostService;

    private ServerConfigRepository serverConfigRepository;

    private ObjectMapper objectMapper;

    public ServerConfig get(ObjectId id) {
        return serverConfigRepository.findById(id).orElse(null);
    }

    public ServerConfig get(String id) {
        return this.get(new ObjectId(id));
    }

    public List<ServerConfig> getAll(State state) {
        ServerConfig params = new ServerConfig();
        params.setState(state.value());

        ExampleMatcher matcher = ExampleMatcher.matching();
        return serverConfigRepository.findAll(Example.of(params, matcher));
    }

    public List<ServerConfig> getAll(State state, ServerConfigQueryDto dto) {
        if (dto == null) {
            return this.getAll(state);
        }

        ServerConfig params = new ServerConfig();
        if (dto.getTypeId() != null) {
            params.setTypeId(new ObjectId(dto.getTypeId()));
        }
        if (dto.getAppId() != null) {
            params.setAppId(dto.getAppId());
        }

        ExampleMatcher matcher = ExampleMatcher.matching();
        return serverConfigRepository.findAll(Example.of(params, matcher));
    }

    public ServerConfigDto toServerConfigDto(ServerConfig serverConfig) {
        ServiceConfigDto configDto = null;
        try {
            configDto = objectMapper.readValue(serverConfig.getContent(), ServiceConfigDto.class);
        } catch (JsonProcessingException ignored) {
        }
        return new ServerConfigDto(serverConfig, configDto);
    }

    public Page<ServerConfig> query(Pageable pageable) throws Exception {
        return serverConfigRepository.findAll(pageable);
    }

    public Page<ServerConfig> query(Pageable pageable, ServerConfigQueryDto dto) throws Exception {
        if (dto == null) {
            return this.query(pageable);
        }

        ServerConfig params = new ServerConfig();
        if (dto.getTypeId() != null) {
            params.setTypeId(new ObjectId(dto.getTypeId()));
        }
        if (dto.getAppId() != null) {
            params.setAppId(dto.getAppId());
        }

        ExampleMatcher matcher = ExampleMatcher.matching();
        return serverConfigRepository.findAll(Example.of(params, matcher), pageable);
    }

    public ServerConfig add(ServerConfigAddDto dto, AdminDetails operator) throws Exception {
        ServerType serverType = serverTypeService.get(new ObjectId(dto.getTypeId()));
        if (serverType == null) {
            throw new StandardRespondException("服务类型不存在");
        }
        if (!State.enable(serverType.getState())) {
            throw new StandardRespondException("服务类型已禁用");
        }

        if (serverConfigRepository.existsByIdNotAndTypeIdAndAppId(null, serverType.getId(), dto.getAppId())) {
            throw new StandardRespondException("服务ID已存在");
        }

        ServerHost serverHost = serverHostService.get(new ObjectId(dto.getHostId()));
        if (serverHost == null) {
            throw new StandardRespondException("主机不存在");
        }
        if (!State.enable(serverHost.getState())) {
            throw new StandardRespondException("主机已禁用");
        }

        try {
            ServiceConfigDto configDto = objectMapper.readValue(dto.getContent(), ServiceConfigDto.class);
            if (!serverType.getName().equals(configDto.getName()) || !dto.getAppId().equals(configDto.getAppId())) {
                throw new StandardRespondException("服务配置不正确");
            }
        } catch (JsonProcessingException ignore) {
            throw new StandardRespondException("服务配置不正确");
        }

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setTypeId(serverType.getId());
        serverConfig.setAppId(dto.getAppId());
        serverConfig.setHostId(serverHost.getId());
        serverConfig.setContent(dto.getContent());
        serverConfig.setState(State.enable.value());
        serverConfig.initOperation(dto.getRemark(), operator.getUsername());

        serverConfig.setServerType(serverType);
        serverConfig.setServerHost(serverHost);

        return serverConfigRepository.save(serverConfig);
    }

    public ServerConfig update(ServerConfigUpdateDto dto, AdminDetails operator) throws Exception {
        ObjectId id = new ObjectId(dto.getId());
        ServerConfig serverConfig = this.get(id);
        if (serverConfig == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        if (dto.getTypeId() != null) {
            ServerType serverType = serverTypeService.get(new ObjectId(dto.getTypeId()));
            if (serverType == null) {
                throw new StandardRespondException("服务类型不存在");
            }
            if (!State.enable(serverType.getState())) {
                throw new StandardRespondException("服务类型已禁用");
            }

            if (serverConfigRepository.existsByIdNotAndTypeIdAndAppId(id, serverType.getId(), dto.getAppId())) {
                throw new StandardRespondException("服务ID已存在");
            }
            serverConfig.setTypeId(serverType.getId());
        }
        if (dto.getAppId() != null) {
            if (serverConfigRepository.existsByIdNotAndTypeIdAndAppId(id, serverConfig.getTypeId(), dto.getAppId())) {
                throw new StandardRespondException("服务ID已存在");
            }
            serverConfig.setAppId(dto.getAppId());
        }
        if (dto.getHostId() != null) {
            ServerHost serverHost = serverHostService.get(new ObjectId(dto.getHostId()));
            if (serverHost == null) {
                throw new StandardRespondException("主机不存在");
            }
            if (serverHost.getState() == State.disable.value()) {
                throw new StandardRespondException("主机已禁用");
            }
            serverConfig.setHostId(serverHost.getId());
            serverConfig.setServerHost(serverHost);
        }
        if (dto.getContent() != null) {
            try {
                ServiceConfigDto configDto = objectMapper.readValue(dto.getContent(), ServiceConfigDto.class);
                if (!serverConfig.getServerType().getName().equals(configDto.getName()) || !serverConfig.getAppId().equals(configDto.getAppId())) {
                    throw new StandardRespondException("服务配置不正确");
                }
            } catch (JsonProcessingException ignore) {
                throw new StandardRespondException("服务配置不正确");
            }

            serverConfig.setContent(dto.getContent());
        }
        serverConfig.updateOperation(dto.getRemark(), operator.getUsername());

        return serverConfigRepository.save(serverConfig);
    }

    public void delete(DeleteDto dto) {
        serverConfigRepository.deleteAllById(dto.getIds().stream().map(ObjectId::new).collect(Collectors.toSet()));
    }

    public void changeState(ChangeStateDto dto, AdminDetails operator) {
        serverConfigRepository.updateState(dto.getIds().stream().map(ObjectId::new).collect(Collectors.toSet()), dto.getState(), operator.getUsername(), LocalDateTime.now());
    }

    public boolean existsByTypeId(ObjectId typeId) {
        return serverConfigRepository.existsAllByTypeId(typeId);
    }

    public boolean existsByHostId(ObjectId hostId) {
        return serverConfigRepository.existsAllByHostId(hostId);
    }
}

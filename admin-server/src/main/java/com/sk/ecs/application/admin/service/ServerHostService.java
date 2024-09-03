package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerType;
import com.sk.ecs.application.admin.repository.ServerConfigRepository;
import com.sk.ecs.application.admin.repository.ServerHostRepository;
import com.sk.ecs.application.common.exception.StandardRespondException;
import com.sk.ecs.application.base.entity.State;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Setter(onMethod_ = @Autowired)
public class ServerHostService {

    private ServerHostRepository serverHostRepository;

    private ServerConfigRepository serverConfigRepository;

    private String serverDir;

    @Value("${ecs.admin.server-dir}")
    public void setServerDir(String serverDir) {
        this.serverDir = serverDir;
    }

    public ServerHost get(ObjectId id) {
        return serverHostRepository.findById(id).orElse(null);
    }

    public ServerHost get(String id) {
        return this.get(new ObjectId(id));
    }

    public List<ServerHost> getAll(State state) {
        return serverHostRepository.findAllByState(state.value());
    }

    public Page<ServerHost> query(Pageable pageable, ServerHostQueryDto dto) throws Exception {
        if (dto == null) {
            return serverHostRepository.findAll(pageable);
        }

        ServerHost params = new ServerHost();
        ExampleMatcher matcher = ExampleMatcher.matching();
        if (dto.getName() != null) {
            params.setName(dto.getName());
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (dto.getHost() != null) {
            params.setHost(dto.getHost());
            matcher = matcher.withMatcher("host", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        return serverHostRepository.findAll(Example.of(params, matcher), pageable);
    }

    public ServerHost add(ServerHostAddDto dto, AdminDetails operator) {
        if (serverHostRepository.existsByIdNotAndName(null, dto.getName())) {
            throw new StandardRespondException("主机名称已存在");
        }
        if (serverHostRepository.existsByIdNotAndHostAndPort(null, dto.getHost(), dto.getPort())) {
            throw new StandardRespondException("主机端口已存在");
        }

        ServerHost serverHost = new ServerHost();
        serverHost.setName(dto.getName());
        serverHost.setHost(dto.getHost());
        serverHost.setPort(dto.getPort() == null || dto.getPort() == 0 ? 22 : dto.getPort());
        serverHost.setDir(dto.getDir());
        serverHost.setUser(dto.getUser());
        serverHost.setPassword(ObjectUtils.defaultIfNull(dto.getPassword(), ""));
        serverHost.setKey(ObjectUtils.defaultIfNull(dto.getKey(), ""));
        serverHost.setState(State.enable.value());
        serverHost.initOperation(dto.getRemark(), operator.getUsername());

        return serverHostRepository.save(serverHost);
    }

    public ServerHost update(ServerHostUpdateDto dto, AdminDetails operator) {
        ObjectId id = new ObjectId(dto.getId());
        ServerHost serverHost = this.get(id);
        if (serverHost == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        if (dto.getName() != null) {
            if (serverHostRepository.existsByIdNotAndName(id, dto.getName())) {
                throw new StandardRespondException("主机名称已存在");
            }
            serverHost.setName(dto.getName());
        }
        if (dto.getHost() != null) {
            if (serverHostRepository.existsByIdNotAndHostAndPort(id, dto.getHost(), dto.getPort())) {
                throw new StandardRespondException("主机端口已存在");
            }
            serverHost.setHost(dto.getHost());
        }
        if (dto.getPort() != null) {
            if (serverHostRepository.existsByIdNotAndHostAndPort(id, dto.getHost(), dto.getPort())) {
                throw new StandardRespondException("主机端口已存在");
            }
            serverHost.setPort(dto.getPort());
        }
        if (dto.getDir() != null) {
            serverHost.setDir(dto.getDir());
        }
        if (dto.getUser() != null) {
            serverHost.setUser(dto.getUser());
        }
        if (dto.getPassword() != null) {
            serverHost.setPassword(dto.getPassword());
        }
        if (dto.getKey() != null) {
            serverHost.setKey(dto.getKey());
        }

        serverHost.updateOperation(dto.getRemark(), operator.getUsername());

        return serverHostRepository.save(serverHost);
    }

    public void delete(DeleteDto dto) {
        serverHostRepository.deleteAllById(dto.getIds().stream().map((id) -> {
            ObjectId hostId = new ObjectId(id);
            if (serverConfigRepository.existsAllByHostId(hostId)) {
                throw new StandardRespondException("请先删除该主机的服务配置");
            }
            return hostId;
        }).collect(Collectors.toSet()));
    }

    public void changeState(ChangeStateDto dto, AdminDetails operator) {
        serverHostRepository.updateState(dto.getIds().stream().map(ObjectId::new).collect(Collectors.toSet()), dto.getState(), operator.getUsername(), LocalDateTime.now());
    }
}

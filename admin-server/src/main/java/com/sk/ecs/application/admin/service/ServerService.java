package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.entity.ServerConfig;
import com.sk.ecs.application.admin.entity.ServerHost;
import com.sk.ecs.application.admin.entity.ServerPublishVersion;
import com.sk.ecs.application.admin.entity.ServerType;
import com.sk.ecs.application.admin.feign.EcsAdminClient;
import com.sk.ecs.application.admin.feign.ack.MsgGateSettingGetAck;
import com.sk.ecs.application.admin.feign.ack.MsgServerDetectAck;
import com.sk.ecs.application.admin.feign.base.MessageReply;
import com.sk.ecs.application.admin.feign.base.ServerDetectData;
import com.sk.ecs.application.admin.feign.req.MsgGateSettingSaveReq;
import com.sk.ecs.application.admin.feign.req.MsgServerDetectReq;
import com.sk.ecs.application.admin.feign.base.ServerDetect;
import com.sk.ecs.application.admin.repository.ServerPublishVersionRepository;
import com.sk.ecs.application.admin.util.ssh.ExecResult;
import com.sk.ecs.application.admin.util.ssh.SshHelper;
import com.sk.ecs.application.base.entity.State;
import com.sk.ecs.application.common.exception.StandardRespondException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
@Setter(onMethod_ = @Autowired)
public class ServerService {

    private ServerTypeService serverTypeService;

    private ServerConfigService serverConfigService;

    private ServerPublishVersionRepository serverPublishVersionRepository;

    private EcsAdminClient ecsAdminClient;

    private String getProgramVersion(ServerConfig config) {
        try {
            return serverTypeService.getProgramVersion(config.getServerType());
        } catch (Exception e) {
            log.error("获取程序版本失败", e);
            return "ERROR";
        }
    }

    private String getDataVersion(ServerConfig config) {
        try {
            return serverTypeService.getDataVersion(config.getServerType());
        } catch (Exception e) {
            log.error("获取数据版本失败", e);
            return "ERROR";
        }
    }

    private String getConfigVersion(ServerConfig config) {
        String hash = DigestUtils.md5DigestAsHex(config.getContent().getBytes());
        return hash.substring(0, 5).toUpperCase();
    }

    private String[] getNewestVersion(ServerConfig config) {
        return new String[]{getProgramVersion(config), getDataVersion(config), getConfigVersion(config)};
    }

    public void savePublishVersion(ServerConfig config, String[] version) {
        String id = String.format("%s_%d", config.getServerType().getName(), config.getAppId());
        ServerPublishVersion publishVersion = serverPublishVersionRepository.findById(id).orElse(null);
        if (publishVersion == null) {
            publishVersion = new ServerPublishVersion();
            publishVersion.setId(id);
        }
        publishVersion.setVersion(version);
        publishVersion.setTime(LocalDateTime.now());

        serverPublishVersionRepository.save(publishVersion);
    }

    public String[] getPublishVersion(ServerConfig config) {
        String id = String.format("%s_%d", config.getServerType().getName(), config.getAppId());
        ServerPublishVersion publishVersion = serverPublishVersionRepository.findById(id).orElse(null);
        if (publishVersion == null) {
            return null;
        }

        if (publishVersion.getTime().isBefore(config.getCreateTime())) {
            return null;
        }

        return publishVersion.getVersion();
    }

    public List<ServiceDto> getList(ServerConfigQueryDto queryDto) throws Exception {
        List<ServerConfig> configList = serverConfigService.getAll(State.enable, queryDto);

        List<ServiceDto> infoList = new ArrayList<>();
        if (!configList.isEmpty()) {
            Map<String, Map<Integer, ServerDetectData>> detectMap = new HashMap<>();
            try {
                MsgServerDetectReq detectReq = new MsgServerDetectReq();
                for (ServerConfig config : configList) {
                    detectReq.getList().add(new ServerDetect(config.getServerType().getName(), config.getAppId()));
                }

                MessageReply<MsgServerDetectAck> reply = ecsAdminClient.serverDetect(detectReq);
                MsgServerDetectAck ack = reply.getData();
                if (reply.getStatus() == 0 && !CollectionUtils.isEmpty(ack.getList())) {
                    for (ServerDetectData detectData : ack.getList()) {
                        detectMap.computeIfAbsent(detectData.getName(), key -> new HashMap<>()).put(detectData.getAppId(), detectData);
                    }
                }
            } catch (RuntimeException e) {
                log.error("服务诊断失败", e);
            }

            for (ServerConfig config : configList) {
                ServerConfigDto configDto = serverConfigService.toServerConfigDto(config);

                ServiceDto serviceDto = new ServiceDto(configDto.getId(), configDto.getTypeId(), configDto.getTypeName(), configDto.getAppId(), configDto.getSvrArea(), configDto.getSvrName());

                serviceDto.setNewestVersion(getNewestVersion(config));
                serviceDto.setPublishedVersion(getPublishVersion(config));
                serviceDto.setRunningVersion(null);
                serviceDto.setStatus(0);

                Map<Integer, ServerDetectData> idMap = detectMap.get(serviceDto.getTypeName());
                if (idMap != null) {
                    ServerDetectData detectData = idMap.get(serviceDto.getAppId());
                    if (detectData != null) {
                        serviceDto.setRunning(detectData);
                    }
                }

                infoList.add(serviceDto);
            }

            infoList.sort((s1, s2) -> {
                if (s1.getTypeName().equals(s2.getTypeName())) {
                    return s1.getAppId().compareTo(s2.getAppId());
                } else {
                    return s1.getTypeName().compareTo(s2.getTypeName());
                }
            });
        }
        return infoList;
    }

    public List<ServiceDto> query(ServerConfigQueryDto queryDto) throws Exception {
        List<ServerConfig> configList = serverConfigService.getAll(State.enable, queryDto);

        List<ServiceDto> infoList = new ArrayList<>();
        if (!configList.isEmpty()) {
            for (ServerConfig config : configList) {
                ServerConfigDto configDto = serverConfigService.toServerConfigDto(config);

                ServiceDto serviceDto = new ServiceDto(configDto.getId(), configDto.getTypeId(), configDto.getTypeName(), configDto.getAppId(), configDto.getSvrArea(), configDto.getSvrName());

                serviceDto.setNewestVersion(getNewestVersion(config));
                serviceDto.setPublishedVersion(getPublishVersion(config));
                serviceDto.setRunning(null);

                infoList.add(serviceDto);
            }

            infoList.sort((s1, s2) -> {
                if (s1.getTypeName().equals(s2.getTypeName())) {
                    return s1.getAppId().compareTo(s2.getAppId());
                } else {
                    return s1.getTypeName().compareTo(s2.getTypeName());
                }
            });
        }
        return infoList;
    }

    public ServiceDetectDto serviceDetect(String configId) throws Exception {
        ServerConfig config = serverConfigService.get(configId);
        if (config == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        ServiceDetectDto detectDto = new ServiceDetectDto();
        detectDto.setRunning(null);

        MsgServerDetectReq detectReq = new MsgServerDetectReq();
        detectReq.getList().add(new ServerDetect(config.getServerType().getName(), config.getAppId()));

        MessageReply<MsgServerDetectAck> reply = ecsAdminClient.serverDetect(detectReq);
        MsgServerDetectAck ack = reply.getData();
        if (reply.getStatus() == 0 && !CollectionUtils.isEmpty(ack.getList())) {
            detectDto.setRunning(ack.getList().get(0));
        }

        return detectDto;
    }

    public String[] publish(ServicePublishDto dto) throws Exception {
        ServerConfig config = serverConfigService.get(dto.getConfigId());
        if (config == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        ServerType type = config.getServerType();
        ServerHost host = config.getServerHost();

        if (!StringUtils.hasText(type.getVersion())) {
            throw new StandardRespondException("请先上传包文件");
        }

        log.debug(String.format("开始压缩包文件, type: %s, appId: %d, version: %s", type.getName(), config.getAppId(), type.getVersion()));
        String packagePath = serverTypeService.compressPackageFile(type.getName(), config.getAppId(), type.getVersion());

        ExecResult execResult;
        SshHelper sshHelper = new SshHelper(host.getHost(), host.getPort(), host.getUser(), host.getPassword(), host.getKey());

        log.debug(String.format("连接SSH服务器, host: %s, port: %s, user: %s", host.getHost(), host.getPort(), host.getUser()));
        sshHelper.connect();

        String hostDir = host.getDir();
        if (!hostDir.endsWith("/")) {
            hostDir += "/";
        }

        String serviceDir = String.format("%s_%d/", config.getServerType().getName(), config.getAppId());

        log.debug(String.format("删除远程目录, hostDir: %s, serviceDir: %s", hostDir, serviceDir));
        execResult = sshHelper.exec("cd", hostDir, "&&", "rm", "-rf", serviceDir);
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        log.debug(String.format("创建远程目录, hostDir: %s, serviceDir: %s", hostDir, serviceDir));
        execResult = sshHelper.exec("cd", hostDir, "&&", "mkdir", serviceDir);
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        log.debug(String.format("创建配置目录, hostDir: %s, serviceDir: %s", hostDir, serviceDir));
        execResult = sshHelper.exec("cd", hostDir, "&&", "mkdir", serviceDir + "conf");
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        String serviceConfig = serviceDir + String.format("conf/%s_%d.json", type.getName(), config.getAppId());

        log.debug(String.format("上传配置文件, hostDir: %s, serviceConfig: %s", hostDir, serviceConfig));
        sshHelper.sftpPut(new ByteArrayInputStream(config.getContent().getBytes()), hostDir + serviceConfig);

        File localFile = new File(packagePath);

        String servicePackage = serviceDir + localFile.getName();

        log.debug(String.format("上传压缩文件, hostDir: %s, removePackage: %s", hostDir, servicePackage));
        sshHelper.sftpPut(localFile, hostDir + servicePackage);

        Thread.sleep(100L);

        log.debug(String.format("删除本地文件, packagePath: %s", packagePath));
        localFile.delete();

        log.debug(String.format("解压压缩文件, hostDir: %s, servicePackage: %s", hostDir, servicePackage));
        execResult = sshHelper.exec("cd", hostDir, "&&", "tar", "-zxf", servicePackage, "-C", serviceDir);
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        log.debug(String.format("删除压缩文件, hostDir: %s, servicePackage: %s", hostDir, servicePackage));
        execResult = sshHelper.exec("cd", hostDir, "&&", "rm", "-f", servicePackage);
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        log.debug(String.format("计算配置版本, hostDir: %s, remoteConfig: %s", hostDir, serviceConfig));
        execResult = sshHelper.exec("cd", hostDir, "&&", "./hash-calc", "-f", serviceConfig);
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        String configVersion = "EMPTY";
        if (execResult.getOutput().length() > 5) {
            configVersion = execResult.getOutput().substring(0, 5);
        }

        log.debug(String.format("计算程序版本, hostDir: %s, binDir: %sbin", hostDir, serviceDir));
        execResult = sshHelper.exec("cd", hostDir, "&&", "./hash-calc", "-m", "-d", serviceDir + "bin");
        if (!execResult.isSuccess()) {
            log.error("命令执行失败, " + execResult.getError());
            log.debug("关闭SSH连接");
            sshHelper.close();
            throw new StandardRespondException("命令执行失败");
        }

        String programVersion = "EMPTY";
        if (execResult.getOutput().length() > 5) {
            programVersion = execResult.getOutput().substring(0, 5);
        }

        String dataVersion = "EMPTY";
        log.debug(String.format("计算数据版本, hostDir: %s, dataDir: %sdata", hostDir, serviceDir));
        execResult = sshHelper.exec("cd", hostDir, "&&", "./hash-calc", "-m", "-d", serviceDir + "data");
        if (execResult.isSuccess()) {
            if (execResult.getOutput().length() > 5) {
                dataVersion = execResult.getOutput().substring(0, 5);
            }
        }

        log.debug("关闭SSH连接");
        sshHelper.close();

        String[] publishVersion = new String[]{programVersion, dataVersion, configVersion};

        savePublishVersion(config, publishVersion);

        return publishVersion;
    }

    public ServiceDetectDto control(ServiceControlDto dto) throws Exception {
        ServerConfig config = serverConfigService.get(dto.getConfigId());
        if (config == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        String appCommand = switch (dto.getOperate()) {
            case 1 -> "start";
            case 2 -> "stop";
            case 3 -> "restart";
            default -> throw new StandardRespondException("未知的命令");
        };

        ServerType type = config.getServerType();
        ServerHost host = config.getServerHost();

        ExecResult execResult;
        SshHelper sshHelper = new SshHelper(host.getHost(), host.getPort(), host.getUser(), host.getPassword(), host.getKey());

        log.debug(String.format("连接SSH服务器, host: %s, port: %s, user: %s", host.getHost(), host.getPort(), host.getUser()));
        sshHelper.connect();

        String hostDir = host.getDir();
        if (!hostDir.endsWith("/")) {
            hostDir += "/";
        }

        log.debug(String.format("执行控制命令, hostDir: %s, appCommand: %s", hostDir, appCommand));
        execResult = sshHelper.exec("cd", hostDir, "&&", "./app.sh", appCommand, type.getName(), config.getAppId().toString());
        if (!execResult.isSuccess()) {
            log.debug("关闭SSH连接");
            sshHelper.close();
            log.error("命令执行失败, " + execResult.getError());
            throw new StandardRespondException("命令执行失败");
        }

        log.debug("关闭SSH连接");
        sshHelper.close();

        ServerDetectData detectData = null;

        if (dto.getOperate() == 2) {
            log.debug("关闭操作不需要诊断");
        } else {
            log.debug("开始服务诊断");
            Thread.sleep(500);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(500);

                try {
                    MsgServerDetectReq detectReq = new MsgServerDetectReq();
                    detectReq.getList().add(new ServerDetect(type.getName(), config.getAppId()));

                    log.debug(String.format("执行第%d次服务诊断", i + 1));
                    MessageReply<MsgServerDetectAck> reply = ecsAdminClient.serverDetect(detectReq);

                    if (reply.getStatus() == 0) {
                        MsgServerDetectAck ack = reply.getData();
                        if (!CollectionUtils.isEmpty(ack.getList())) {
                            detectData = ack.getList().get(0);
                            log.debug("服务诊断成功，不再重试");
                            break;
                        }
                    }
                } catch (RuntimeException e) {
                    log.error("服务诊断失败", e);
                    break;
                }
            }
        }

        ServiceDetectDto runningDto = new ServiceDetectDto();
        runningDto.setRunning(detectData);

        return runningDto;
    }

    public GateSettingDto getGateSetting() throws Exception {
        MessageReply<MsgGateSettingGetAck> reply = ecsAdminClient.getGateSetting();
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("获取网关设置失败：" + reply.getMessage());
        }

        MsgGateSettingGetAck ack = reply.getData();

        return new GateSettingDto(ack);
    }

    public GateSettingDto saveGateSetting(GateSettingDto dto) throws Exception {
        List<Integer> closedList = null;
        if (StringUtils.hasText(dto.getClosedList())) {
            closedList = Stream.of(dto.getClosedList().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        }

        List<Long> whiteList = null;
        if (StringUtils.hasText(dto.getWhiteList())) {
            whiteList = Stream.of(dto.getWhiteList().split(",")).map(Long::valueOf).collect(Collectors.toList());
        }

        boolean loginClosed = Boolean.TRUE.equals(dto.getLoginClosed());
        boolean offline = loginClosed && Boolean.TRUE.equals(dto.getOffline());
        boolean timedClose = loginClosed & Boolean.TRUE.equals(dto.getTimedClose());

        long closeTime = 0L;
        if (timedClose && dto.getCloseDate() != null && dto.getCloseTime() != null) {
            closeTime = LocalDateTime.of(dto.getCloseDate(), dto.getCloseTime()).toEpochSecond(ZoneOffset.ofHours(8));
        }

        MsgGateSettingSaveReq req = new MsgGateSettingSaveReq(dto.getVersion(), dto.getTitle(), dto.getContent(), loginClosed, offline, timedClose, closeTime, closedList, whiteList);

        MessageReply<?> reply = ecsAdminClient.saveGateSetting(req);
        if (reply.getStatus() != 0) {
            throw new StandardRespondException("保存网关设置失败：" + reply.getMessage());
        }

        return new GateSettingDto(req);
    }
}

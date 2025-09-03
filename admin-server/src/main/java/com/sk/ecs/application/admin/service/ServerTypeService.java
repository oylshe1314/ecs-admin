package com.sk.ecs.application.admin.service;

import com.sk.ecs.application.admin.auth.AdminDetails;
import com.sk.ecs.application.admin.dto.*;
import com.sk.ecs.application.admin.entity.ServerType;
import com.sk.ecs.application.admin.repository.ServerConfigRepository;
import com.sk.ecs.application.admin.repository.ServerTypeRepository;
import com.sk.ecs.application.base.entity.State;
import com.sk.ecs.application.common.exception.StandardRespondException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
@Setter(onMethod_ = @Autowired)
public class ServerTypeService {

    private ServerTypeRepository serverTypeRepository;

    private ServerConfigService serverConfigService;

    private String serverDir;

    @Value("${ecs.admin.server-dir}")
    public void setServerDir(String serverDir) {
        this.serverDir = serverDir;
    }

    public ServerType get(ObjectId id) {
        return serverTypeRepository.findById(id).orElse(null);
    }

    public ServerType get(String id) {
        return this.get(new ObjectId(id));
    }

    public List<ServerType> getAll(State state) {
        return serverTypeRepository.findAllByState(state.value());
    }

    public Page<ServerType> query(Pageable pageable, ServerTypeQueryDto dto) throws Exception {
        if (dto == null) {
            return serverTypeRepository.findAll(pageable);
        }

        ServerType params = new ServerType();
        ExampleMatcher matcher = ExampleMatcher.matching();
        if (dto.getName() != null) {
            params.setName(dto.getName());
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        return serverTypeRepository.findAll(Example.of(params, matcher), pageable);
    }

    public ServerType add(ServerTypeAddDto dto, AdminDetails operator) {
        if (serverTypeRepository.existsByIdNotAndName(null, dto.getName())) {
            throw new StandardRespondException("服务类型名称已存在");
        }

        ServerType serverType = new ServerType();
        serverType.setName(dto.getName());
        serverType.setVersion("");
        serverType.setDescription(dto.getDescription());
        serverType.setState(State.enable.value());
        serverType.initOperation(dto.getRemark(), operator.getUsername());

        return serverTypeRepository.save(serverType);
    }

    public ServerType update(ServerTypeUpdateDto dto, AdminDetails operator) {
        ObjectId id = new ObjectId(dto.getId());
        ServerType serverType = this.get(id);
        if (serverType == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        if (dto.getName() != null) {
            if (serverTypeRepository.existsByIdNotAndName(id, dto.getName())) {
                throw new StandardRespondException("服务类型名称已存在");
            }
            serverType.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            serverType.setDescription(dto.getDescription());
        }

        serverType.updateOperation(dto.getRemark(), operator.getUsername());

        return serverTypeRepository.save(serverType);
    }

    public void delete(DeleteDto dto) {
        serverTypeRepository.deleteAllById(dto.getIds().stream().map((id) -> {
            ObjectId typeId = new ObjectId(id);
            if (serverConfigService.existsByTypeId(typeId)) {
                throw new StandardRespondException("请先删除该类型的服务配置");
            }
            return typeId;
        }).collect(Collectors.toSet()));
    }

    public void changeState(ChangeStateDto dto, AdminDetails operator) {
        serverTypeRepository.updateState(dto.getIds().stream().map(ObjectId::new).collect(Collectors.toSet()), dto.getState(), operator.getUsername(), LocalDateTime.now());
    }

    private void decompressPackageFile(String name, String version, MultipartFile upload) throws Exception {
        if (upload.getOriginalFilename() == null || !upload.getOriginalFilename().endsWith(".zip")) {
            throw new StandardRespondException("包格式错误");
        }

        byte[] head = upload.getInputStream().readNBytes(4);
        if (!Arrays.equals(head, new byte[]{'P', 'K', 0x03, 0x04})) {
            throw new StandardRespondException("包格式错误");
        }

        String dir = String.format("%s/package/%s/%s", serverDir, name, version);

        File parentDir = new File(dir);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        } else {
            if (!parentDir.isDirectory()) {
                parentDir.delete();
                parentDir.mkdirs();
            } else {
                File[] children = parentDir.listFiles();
                if (children != null) {
                    for (File child : children) {
                        child.delete();
                    }
                }
            }
        }

        File file = new File(dir + "/package.zip");

        upload.transferTo(file);

        String[] cmd;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cmd = new String[]{"tar", "-zxf", "package.zip"};
        } else {
            cmd = new String[]{"unzip", "-o", "package.zip"};
        }

        Process process = Runtime.getRuntime().exec(cmd, null, parentDir);
        int status = process.waitFor();
        if (status != 0) {
            log.error("包解压失败, " + new String(process.getErrorStream().readAllBytes()));
            throw new StandardRespondException("包解压失败");
        }

        file.delete();
    }

    protected String compressPackageFile(String name, Integer appId, String version) throws Exception {
        String dir = String.format("%s/package/%s", serverDir, name);
        File parentDir = new File(dir);
        if (!parentDir.exists()) {
            throw new StandardRespondException("请先上传包文件");
        }

        File versionDir = new File(dir + "/" + version);
        if (!versionDir.exists()) {
            throw new StandardRespondException("版本包不存在");
        }

        String[] filenames = versionDir.list();
        if (filenames == null) {
            throw new StandardRespondException("版本包不存在");
        }

        String packageFile = String.format("%s_%d-%s.tar.gz", name, appId, version);
        String packagePath = dir + "/" + packageFile;
        File file = new File(packagePath);
        if (file.exists()) {
            file.delete();
        }

        String[] cmd = new String[5 + filenames.length];
        cmd[0] = "tar";
        cmd[1] = "-zcf";
        cmd[2] = packageFile;
        cmd[3] = "-C";
        cmd[4] = version;
        System.arraycopy(filenames, 0, cmd, 5, filenames.length);
        Process process = Runtime.getRuntime().exec(cmd, null, parentDir);
        int status = process.waitFor();
        if (status != 0) {
            log.error(String.format("包压缩失败, cmd: %s, error: %s", String.join(" ", cmd), new String(process.getErrorStream().readAllBytes())));
            throw new StandardRespondException("包压缩失败");
        }

        return packagePath;
    }

    public void upload(ServerPackageUploadDto dto) throws Exception {
        ObjectId id = new ObjectId(dto.getTypeId());
        ServerType serverType = this.get(id);
        if (serverType == null) {
            throw new StandardRespondException("ID找不到记录");
        }

        serverType.setVersion(dto.getVersion());

        this.decompressPackageFile(serverType.getName(), serverType.getVersion(), dto.getFile());

        serverTypeRepository.updateVersion(serverType.getId(), serverType.getVersion());
    }

    protected String getProgramVersion(ServerType serverType) throws Exception {
        if (!StringUtils.hasText(serverType.getVersion())) {
            return "EMPTY";
        }

        String dir = String.format("package/%s/%s/bin", serverType.getName(), serverType.getVersion());
        if (!new File(serverDir + "/" + dir).exists()) {
            return "EMPTY";
        }

        return getDirHash("./" + dir).substring(0, 5).toUpperCase();
    }

    protected String getDataVersion(ServerType serverType) throws Exception {
        if (!StringUtils.hasText(serverType.getVersion())) {
            return "EMPTY";
        }

        String dir = String.format("package/%s/%s/data", serverType.getName(), serverType.getVersion());
        if (!new File(serverDir + "/" + dir).exists()) {
            return "EMPTY";
        }

        return getDirHash("./" + dir).substring(0, 5).toUpperCase();
    }

    private String getDirHash(String dir) throws Exception {
        String[] cmd;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            cmd = new String[]{"cmd", "/c", "hash-calc", "-m", "-d", dir};
        } else {
            cmd = new String[]{"./hash-calc", "-m", "-d", dir};
        }

        Process process = Runtime.getRuntime().exec(cmd, null, new File(serverDir));
        int status = process.waitFor();
        if (status != 0) {
            throw new Exception(new String(process.getErrorStream().readAllBytes()));
        }

        return new String(process.getInputStream().readAllBytes());
    }
}

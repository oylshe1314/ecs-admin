package com.sk.ecs.application.admin.dto;

import com.sk.ecs.application.admin.feign.base.ServerDetectData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ServiceDetectDto {

    @Schema(title = "运行版本")
    private String[] runningVersion;

    @Schema(title = "状态, 0.已关闭, 1.运行中")
    private Integer status;

    @Schema(title = "PID")
    private Integer pid;

    @Schema(title = "CPU")
    private String cpu;

    @Schema(title = "内存")
    private String memory;

    @Schema(title = "在线")
    private Integer online;

    @Schema(title = "协程")
    private Integer coroutine;

    @Schema(title = "其他信息")
    private String info;

    public void setRunning(ServerDetectData detectData) {
        if (detectData == null) {
            this.setRunningVersion(null);
            this.setStatus(0);
        } else {
            this.setRunningVersion(new String[]{detectData.getProgramHash().substring(0, 5), detectData.getDataHash().substring(0, 5), detectData.getConfigHash().substring(0, 5)});
            this.setStatus(1);
            this.setPid(detectData.getPid());
            if (detectData.getCpu() != null && detectData.getCpu() > 0) {
                String cpu = String.format("%.2f", detectData.getCpu());
                if (cpu.endsWith(".00")) {
                    cpu = cpu.substring(0, cpu.length() - 3);
                } else if (cpu.endsWith("0")) {
                    cpu = cpu.substring(0, cpu.length() - 1);
                }
                this.setCpu(cpu + "%");
            } else {
                this.setCpu("0");
            }
            if (detectData.getMemory() != null && detectData.getMemory() > 0) {
                String mem = String.format("%.2f", detectData.getCpu());
                if (mem.endsWith(".00")) {
                    mem = mem.substring(0, mem.length() - 3);
                } else if (mem.endsWith("0")) {
                    mem = mem.substring(0, mem.length() - 1);
                }
                this.setMemory(mem + "%");
            } else {
                this.setMemory("0");
            }
            this.setOnline(detectData.getOnline());
            this.setCoroutine(detectData.getCoroutine());
            this.setInfo(detectData.getInfo());
        }
    }
}

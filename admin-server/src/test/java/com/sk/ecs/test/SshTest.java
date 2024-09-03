package com.sk.ecs.test;

import com.sk.ecs.application.admin.util.ssh.ExecResult;
import com.sk.ecs.application.admin.util.ssh.SshHelper;

public class SshTest {

    public static void main(String[] args) {

        SshHelper sshHelper = new SshHelper("192.168.2.3", 22, "sk", "456123", null);

        try {
            sshHelper.connect();

            SshShell shell = sshHelper.shell();

            ExecResult execResult;

            shell.open();

//            execResult = shell.exec("cd", "/home/sk/ecs/servers");
//            if (!execResult.isSuccess()) {
//                System.out.println(execResult.getError());
//                shell.close();
//                sshHelper.close();
//                return;
//            }

            execResult = shell.exec("ls");
            if (!execResult.isSuccess()) {
                System.out.println(execResult.getError());
                shell.close();
                sshHelper.close();
                return;
            }

            System.out.println(execResult.getOutput());

            shell.close();
            sshHelper.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.sk.ecs.application.admin.util.ssh;

import lombok.extern.log4j.Log4j2;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.impl.DefaultSftpClientFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.util.StringUtils;

import java.io.*;
import java.security.KeyPair;
import java.util.Set;

@Log4j2
public class SshHelper {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String key;

    private SshClient client;
    private ClientSession session;

    public SshHelper(String host, int port, String user, String password, String key) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.key = key;
    }

    public void connect() throws Exception {
        if (this.session != null) {
            return;
        }

        this.client = SshClient.setUpDefaultClient();
        this.client.start();

        ConnectFuture connectFuture = client.connect(this.user, this.host, this.port).verify();
        this.session = connectFuture.getSession();
        if (this.session == null) {
            log.error("获取Session失败", connectFuture.getException());
            throw new Exception(connectFuture.getException());
        }

        if (StringUtils.hasText(this.password)) {
            this.session.addPasswordIdentity(this.password);
        }

        if (StringUtils.hasText(this.key)) {
//            throw new StandardRespondException("暂时不支付密钥连接SSH");
            PEMParser pemParser = new PEMParser(new StringReader(this.key));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair keyPair = converter.getKeyPair((PEMKeyPair) pemParser.readObject());
            this.session.addPublicKeyIdentity(keyPair);
        }

        AuthFuture authFuture = session.auth().verify();
        if (!authFuture.isSuccess()) {
            log.error("SSH用户认证失败", authFuture.getException());
            throw new Exception(authFuture.getException());
        }
    }

    public void close() throws Exception {
        if (this.session != null && this.session.isOpen()) {
            this.session.close();
            this.session = null;
        }
        if (this.client != null) {
            this.client.stop();
            this.client.close();
            this.client = null;
        }
    }

    public ExecResult exec(String... args) throws Exception {
        if (this.session == null) {
            log.error("SSH未连接");
            throw new Exception("SSH has not connected");
        }

        String command = String.join(" ", args);
        ChannelExec exec = session.createExecChannel(command);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        exec.setOut(out);
        exec.setErr(err);

        OpenFuture openFuture = exec.open().verify();
        if (!openFuture.isOpened()) {
            log.error("ExecChannel打开失败", openFuture.getException());
            throw new Exception(openFuture.getException());
        }

        Set<ClientChannelEvent> eventSet = exec.waitFor(Set.of(ClientChannelEvent.CLOSED, ClientChannelEvent.TIMEOUT), 15000L);

        out.close();
        err.close();
        exec.close();

        if (eventSet.contains(ClientChannelEvent.TIMEOUT)) {
            log.error("执行超时");
            throw new Exception("执行超时");
        }

        return new ExecResult(exec.getExitStatus(), out.toString(), err.size() == 0 ? out.toString() : err.toString());
    }

    public void sftpPut(String localFile, String remotePath) throws Exception {
        this.sftpPut(new File(localFile), remotePath);
    }

    public void sftpPut(File localFile, String remotePath) throws Exception {
        if (remotePath.endsWith("/")) {
            remotePath += localFile.getName();
        }
        this.sftpPut(new FileInputStream(localFile), remotePath);
    }

    public void sftpPut(InputStream is, String remoteFile) throws Exception {
        if (this.session == null) {
            log.error("SSH未连接");
            throw new Exception("SSH has not connected");
        }

        if (remoteFile.endsWith("/")) {
            log.error("文件名称不能以/结尾");
            throw new Exception("Missing the target file name");
        }

        DefaultSftpClientFactory sftpClientFactory = new DefaultSftpClientFactory();
        SftpClient sftpClient = sftpClientFactory.createSftpClient(this.session);

        OutputStream os = sftpClient.write(remoteFile);

        is.transferTo(os);

        is.close();
        os.close();
        sftpClient.close();
    }
}

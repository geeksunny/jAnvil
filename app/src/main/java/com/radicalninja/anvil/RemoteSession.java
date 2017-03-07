package com.radicalninja.anvil;

import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class RemoteSession {

    private final String server;
    private final int port;
    private final String username;

    private final Shell shell;

    public RemoteSession(String server, String username, String keyPath) throws IOException {
        this(server, SSH.PORT, username, keyPath);
    }

    public RemoteSession(String server, int port, String username, String keyPath) throws IOException {
        this.server = server;
        this.port = port;
        this.username = username;

        final File keyFile = new File(keyPath);
        final String privateKey = new String(Files.readAllBytes(keyFile.toPath()));

        shell = new SSH(server, port, username, privateKey);
    }

    public void executeShellCommand(final String[] cmd) throws IOException {
        final String command = String.join(" ", cmd);
        executeShellCommand(command);
    }

    public void executeShellCommand(final String cmd) throws IOException {
        final OutputStream bos = new BufferedOutputStream(System.out);
        final int result = shell.exec(cmd, null, bos, bos);
        // Should we consume the result value?
    }

}

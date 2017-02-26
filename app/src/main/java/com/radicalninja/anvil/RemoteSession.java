package com.radicalninja.anvil;

import com.jcabi.log.Logger;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

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

        shell = new SSH(server, port, username, keyPath);
    }

    public void executeShellCommand(final String[] cmd) throws IOException {
        final String command = String.join(" ", cmd);
        executeShellCommand(command);
    }

    public void executeShellCommand(final String cmd) throws IOException {
        final int result = shell.exec(cmd, null,//NULL OK HERE?
                Logger.stream(Level.INFO, this),
                Logger.stream(Level.WARNING, this));
        Logger.stream(Level.INFO, this);
    }

}

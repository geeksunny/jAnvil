package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

import java.io.*;
import java.nio.file.Path;

public class ExecRemoteSession extends RemoteSession<ChannelExec> {

    public ExecRemoteSession(String server, int port, String username, String password) throws JSchException {
        super(server, port, username, password);
    }

    public ExecRemoteSession(String server, int port, String username, Path keyPath) throws JSchException {
        super(server, port, username, keyPath);
    }

    public ExecRemoteSession(String server, int port, String username, Path keyPath, String passphrase) throws JSchException {
        super(server, port, username, keyPath, passphrase);
    }

    @Override
    protected ChannelExec openChannel() throws JSchException {
        return (ChannelExec) getSession().openChannel("exec");
    }

    public int exec(final String[] cmd) throws IOException, JSchException {
        final String command = String.join(" ", cmd);
        return exec(command);
    }

    public int exec(final String cmd) throws JSchException, IOException {

        final ChannelExec channel = openChannel();
        channel.setCommand(cmd);
        final OutputStream bos = new BufferedOutputStream(System.out);
        channel.setErrStream(bos);
        channel.setInputStream(null);
        final BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.connect();

        String text;
        while ((text = in.readLine()) != null) {
            System.out.println(text);
        }

        final int exitStatus = channel.getExitStatus();
        channel.disconnect();
        return exitStatus;
    }

}

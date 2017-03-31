package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.*;
import com.radicalninja.anvil.util.HomeFile;
import com.radicalninja.anvil.util.TextUtils;

import java.io.File;

public abstract class RemoteSession<T extends Channel> {

    private Session session;

    public RemoteSession(final String server, final int port, final String username,
                         final String password) throws JSchException {
        this(server, port, username, null, null, password);
    }

    public RemoteSession(final String server, final int port, final String username,
                         final File prvKey) throws JSchException {
        this(server, port, username, prvKey, null, null);
    }

    public RemoteSession(final String server, final int port, final String username,
                         final File prvKey, final String passphrase) throws JSchException {
        this(server, port, username, prvKey, passphrase, null);
    }

    private RemoteSession(final String server, final int port, final String username,
                          final File prvKey, final String passphrase, final String password) throws JSchException {
        final JSch jSch = new JSch();

        final File knownHosts = new HomeFile("~/.ssh/known_hosts"); // todo: paramaterize?
        if (knownHosts.exists() && knownHosts.isFile()) {
            jSch.setKnownHosts(knownHosts.getAbsolutePath());
        }

        if (null != prvKey) {
            if (!TextUtils.isEmpty(passphrase)) {
                jSch.addIdentity(prvKey.getPath(), passphrase);
            } else {
                jSch.addIdentity(prvKey.getPath());
            }
        }

        session = jSch.getSession(username, server, port);
        session.setConfig("server_host_key","ecdsa-sha2-nistp256"); // SSH's default preferred algorithm for hostnames. For compatibility with ~/.ssh/known_hosts

        if (!TextUtils.isEmpty(password)) {
            session.setPassword(password);
        }
    }

    protected abstract T openChannel() throws JSchException;

    public void connect() throws JSchException {
        session.connect();
    }

    public void connect(final int timeout) throws JSchException {
        session.connect(timeout);
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public void close() {
        session.disconnect();
    }

    protected Session getSession() {
        return session;
    }

}

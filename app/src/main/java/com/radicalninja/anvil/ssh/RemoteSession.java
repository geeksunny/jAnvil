package com.radicalninja.anvil.ssh;

import com.jcraft.jsch.*;
import com.radicalninja.anvil.HomeFile;

import java.io.File;
import java.nio.file.Path;

public abstract class RemoteSession<T extends Channel> {

    private Session session;

    public RemoteSession(final String server, final int port, final String username,
                         final String password) throws JSchException {
        this(server, port, username, null, AuthInfo.withPassword(password));
    }

    public RemoteSession(final String server, final int port, final String username,
                         final Path keyPath) throws JSchException {
        this(server, port, username, keyPath, (AuthInfo) null);
    }

    public RemoteSession(final String server, final int port, final String username,
                         final Path keyPath, final String passphrase) throws JSchException {
        this(server, port, username, keyPath, AuthInfo.withPassphrase(passphrase));
    }

    private RemoteSession(final String server, final int port, final String username,
                          final Path keyPath, final UserInfo userInfo) throws JSchException {
        final JSch jSch = new JSch();

        final File knownHosts = new HomeFile("~/.ssh/known_hosts"); // todo: paramaterize?
        if (knownHosts.exists() && knownHosts.isFile()) {
            jSch.setKnownHosts(knownHosts.getAbsolutePath());
        }

        if (null != keyPath) {
            jSch.addIdentity(keyPath.toString());
        }

        session = jSch.getSession(username, server, port);
        session.setConfig("server_host_key","ecdsa-sha2-nistp256"); // SSH's default preferred algorithm for hostnames. For compatibility with ~/.ssh/known_hosts

        if (null != userInfo) {
            session.setUserInfo(userInfo);
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

    private static class AuthInfo implements UserInfo {

        static AuthInfo withPassword(final String password) {
            final AuthInfo authInfo = new AuthInfo();
            authInfo.password = password;
            return authInfo;
        }

        static AuthInfo withPassphrase(final String passphrase) {
            final AuthInfo authInfo = new AuthInfo();
            authInfo.passphrase = passphrase;
            return authInfo;
        }

        private String password, passphrase;

        @Override
        public String getPassphrase() {
            return passphrase;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        @Override public void showMessage(String message) { }
    }

}

package com.radicalninja.anvil.tool;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.radicalninja.anvil.util.HomeFile;
import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.ssh.SftpRemoteSession;

import java.io.File;
import java.io.FileNotFoundException;

public class FilePuller extends Tool {

    private SftpRemoteSession session;

    public FilePuller(Configuration configuration) throws JSchException {
        super(configuration);
        connect();
    }

    protected boolean connect() throws JSchException {
        if (null == session) {
            // TODO: Add checks here for any/all required data in the configuration.
            final RemoteConfig config = getConfiguration().getRemoteConfig();
            final File prvKey = new HomeFile(config.getPrivateKey());
            session = new SftpRemoteSession(config.getServer(), config.getPort(), config.getUsername(), prvKey);
        }
        if (!session.isConnected()) {
            session.connect();
        }
        return session.isConnected();
    }

    public boolean pullFile(final String remotePath, final File localDestination) throws InterruptedException, FileNotFoundException, SftpException, JSchException {
        final File result = session.get(remotePath, localDestination.getPath());
        return result.exists();
    }

}

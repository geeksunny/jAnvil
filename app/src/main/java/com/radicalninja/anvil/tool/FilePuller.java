package com.radicalninja.anvil.tool;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.radicalninja.anvil.config.ProjectConfig;
import com.radicalninja.anvil.util.HomeFile;
import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.ssh.SftpRemoteSession;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.File;
import java.io.IOException;

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

    public boolean pullFile(final String remotePath, final File localDestination)
            throws InterruptedException, IOException, SftpException, JSchException {
        final File result = session.get(remotePath, localDestination.getPath());
        return result.exists();
    }

    public File pullBuildResult() throws InterruptedException, IOException, SftpException, JSchException {
        final ProjectConfig projectConfig = getConfiguration().getProjectConfig();
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        // Constructing remoteSrcPath
        // TODO: Work in remote shell home path using the RemoteSession somehow. Build it in upstream.
        final String remoteParentDir = remoteConfig.getDestinationPath().replace("~", "/home/worker");
        final String remoteResultDir = projectConfig.getRemoteResultDirectory();
        final String remoteFilename = projectConfig.getRemoteResultFile();
        final String remoteSrcPath = SystemUtils.createPath(remoteParentDir, destDirName, remoteResultDir, remoteFilename);
        // Constructing localFile
        final String localParentPath = projectConfig.getPath();
        final String tempDirName = getConfiguration().getAnvilConfig().getTempDirName();
        final String localDestPath = SystemUtils.createPath(localParentPath, tempDirName);
        final File localDestFile = new HomeFile(localDestPath, remoteFilename);
        // Pulling file
        final boolean result = pullFile(remoteSrcPath, localDestFile);
        // TODO: Check result and throw some kind of error or warning message
        return localDestFile;
    }

}

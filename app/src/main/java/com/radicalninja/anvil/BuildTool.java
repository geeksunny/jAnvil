package com.radicalninja.anvil;

import com.jcraft.jsch.JSchException;
import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.GradleConfig;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.ssh.ExecRemoteSession;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.File;
import java.io.IOException;

public class BuildTool extends Tool {

    private static final String BUILD_CMD_TEMPLATE = "%s -p %s %s";

    private ExecRemoteSession session;

    public BuildTool(Configuration configuration) {
        super(configuration);
    }

    public void executeGradleTask(final String gradleTask) throws IOException, JSchException {
        createRemoteSession();
        final String cmd = createBuildCommand(gradleTask);
        session.exec(cmd);
    }

    private void createRemoteSession() throws IOException, JSchException {
        if (null != session && session.isConnected()) {
            return;
        }
        final RemoteConfig config = getConfiguration().getRemoteConfig();
        final File prvKey = new HomeFile(config.getPrivateKey());
        session = new ExecRemoteSession(config.getServer(), config.getPort(), config.getUsername(), prvKey);
    }

    private String createBuildCommand(final String gradleTask) {
        final String destination = SystemUtils.createPath(getConfiguration().getRemoteConfig().getDestinationPath(), destDirName);
        final GradleConfig config = getConfiguration().getGradleConfig();
        final String gradlew = SystemUtils.createPath(destination, config.getBuildWrapperFilename());
        return String.format(BUILD_CMD_TEMPLATE, gradlew, destination, gradleTask);
    }

}

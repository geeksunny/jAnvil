package com.radicalninja.anvil;

import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.GradleConfig;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.IOException;

public class BuildTool extends Tool {

    private static final String BUILD_CMD_TEMPLATE = "%s -p %s %s";

    public BuildTool(Configuration configuration) {
        super(configuration);
    }

    public void executeGradleTask(final String gradleTask) throws IOException {
        final RemoteSession session = createRemoteSession();
        final String cmd = createBuildCommand(gradleTask);
        session.executeShellCommand(cmd);
    }

    private RemoteSession createRemoteSession() throws IOException {
        // TODO: does keyfile path need preparation? Should it be a File object?
        final RemoteConfig config = getConfiguration().getRemoteConfig();
        final String keyPath = HomeFile.expandHomePath(config.getPublicKey());
        return new RemoteSession(config.getServer(), config.getPort(), config.getUsername(), keyPath);
    }

    private String createBuildCommand(final String gradleTask) {
        final String destination = SystemUtils.createPath(getConfiguration().getRemoteConfig().getDestinationPath(), destDirName);
        final GradleConfig config = getConfiguration().getGradleConfig();
        final String gradlew = SystemUtils.createPath(destination, config.getBuildWrapperFilename());
        return String.format(BUILD_CMD_TEMPLATE, gradlew, destination, gradleTask);
    }

}

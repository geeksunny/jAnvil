package com.radicalninja.anvil;

import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.ProjectConfig;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SyncTool extends Tool {

    private static final String SSH_PORT_TEMPLATE = "ssh -p %d";
    private static final String RSYNC_PATH_TEMPLATE = "%s@%s:%s";
    private static final String EXCLUDE_FILE_TEMPLATE = "--exclude=%s";
    private static final String EXCLUDE_FROM_TEMPLATE = "--exclude-from=%s";

    public SyncTool(Configuration configuration) {
        super(configuration);
    }

//    private void

    private List<String> makeRsyncCommand() throws IOException {
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        final String sshPort = String.format(SSH_PORT_TEMPLATE, remoteConfig.getPort());
        final List<String> cmd = new LinkedList<>();
        ArrayUtils.addAll(cmd, "rsync", "-zP", "-e", sshPort, "-r");

        final ProjectConfig projectConfig = getConfiguration().getProjectConfig();
        // TODO: null checks for lists below
        for (final String exclude : projectConfig.getExcludeFiles()) {
            final String arg = String.format(EXCLUDE_FILE_TEMPLATE, exclude);
            cmd.add(arg);
        }
        for (final String exclude : projectConfig.getExcludeFromFiles()) {
            final String arg = String.format(EXCLUDE_FROM_TEMPLATE, exclude);
            cmd.add(arg);
        }
        cmd.add(projectConfig.getDirectoryName());

        final File remotePath = new HomeFile(projectConfig.getRemoteResultDirectory());
        cmd.add(rsyncRemotePath(remotePath));
        return cmd;
    }

    private String rsyncRemotePath(final File remoteFileDirectory) throws IOException {
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        return String.format(RSYNC_PATH_TEMPLATE, remoteConfig.getUsername(),
                remoteConfig.getServer(), remoteFileDirectory.getCanonicalPath());
    }

}

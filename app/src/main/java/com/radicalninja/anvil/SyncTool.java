package com.radicalninja.anvil;

import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.GradleConfig;
import com.radicalninja.anvil.config.ProjectConfig;
import com.radicalninja.anvil.config.RemoteConfig;
import com.radicalninja.anvil.data.GradleProperties;
import com.radicalninja.anvil.data.LocalProperties;
import com.radicalninja.anvil.data.Properties;
import com.radicalninja.anvil.util.ArrayUtils;
import com.radicalninja.anvil.util.SystemUtils;
import com.radicalninja.anvil.util.TextUtils;
import org.apache.commons.lang3.StringUtils;

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

    public void doSyncOperations() throws IOException {
        syncSource();
        // TODO: Clean this up
        updateGradleProperties();
        updateLocalProperties();
    }

    public void syncSource() {
        final List<String> cmd;
        try {
            cmd = createProjectSyncCommand();
            SystemUtils.executeShellCommand(cmd);
        } catch (IOException e) {
            // TODO: Error state handling
            e.printStackTrace();
        }
    }

    private List<String> createBaseSyncCommand() throws IOException {
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        final String sshPort = String.format(SSH_PORT_TEMPLATE, remoteConfig.getPort());
        final List<String> cmd = new LinkedList<>();
        ArrayUtils.addAll(cmd, "rsync", "-zP", "-e", sshPort);
        return cmd;
    }

    private List<String> createProjectSyncCommand() throws IOException {
        final List<String> cmd = createBaseSyncCommand();
        cmd.add("-r");
        final ProjectConfig projectConfig = getConfiguration().getProjectConfig();
        TextUtils.addFormattedStrings(cmd, projectConfig.getExcludeFiles(), EXCLUDE_FILE_TEMPLATE);
        TextUtils.addFormattedStrings(cmd, projectConfig.getExcludeFromFiles(), EXCLUDE_FROM_TEMPLATE);
        // TODO: Clean this junk up. Should probably go into a method?
        final File dest = new HomeFile(projectConfig.getPath());
        destDirName = dest.getName();
        // TODO: should we use dest below, too?
        // No trailing slash = rsync whole dir at once
        final String projectPath = StringUtils.stripEnd(projectConfig.getPath(), "/");
        cmd.add(HomeFile.expandHomePath(projectPath));

        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        cmd.add(rsyncRemotePath(remoteConfig.getDestinationPath()));
//        final String full = String.join(" ", cmd);
//        System.out.printf("\n\n%s\n\n", full);
        return cmd;
    }

    // TODO: Is this version necessary?
    private String rsyncRemotePath(final File remoteFileDirectory) throws IOException {
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        return String.format(RSYNC_PATH_TEMPLATE, remoteConfig.getUsername(),
                remoteConfig.getServer(), remoteFileDirectory.getCanonicalPath());
    }

    private String rsyncRemotePath(final String remotePath) {
        final RemoteConfig remoteConfig = getConfiguration().getRemoteConfig();
        return String.format(RSYNC_PATH_TEMPLATE, remoteConfig.getUsername(),
                remoteConfig.getServer(), remotePath);
    }

    private String rsyncRemotePath(final String remotePath, final String filename) {
        return rsyncRemotePath(SystemUtils.createPath(remotePath, filename));
    }

    private void updateGradleProperties() throws IOException {
        final GradleConfig gradleConfig = getConfiguration().getGradleConfig();
        final GradleProperties properties = new GradleProperties(gradleConfig);
        final List<String> cmd = generateAndSyncFileCommand(properties, gradleConfig.getPropertiesPathRemote());
        SystemUtils.executeShellCommand(cmd);
    }

    private void updateLocalProperties() throws IOException {
        final GradleConfig gradleConfig = getConfiguration().getGradleConfig();
        final LocalProperties properties = new LocalProperties(gradleConfig);
        final List<String> cmd = generateAndSyncFileCommand(properties, gradleConfig.getProjectLocalPropertiesFilename());
        SystemUtils.executeShellCommand(cmd);
    }

    private List<String> generateAndSyncFileCommand(final Properties properties, final String filename) throws IOException {
        final String grandparent = getConfiguration().getProjectConfig().getPath();
        final String tempDirName = getConfiguration().getAnvilConfig().getTempDirName();
        final String parent = SystemUtils.createPath(grandparent, tempDirName);
        final File file = new HomeFile(parent, filename);
        if (file.exists()) {
            final String newMd5 = SystemUtils.md5(properties.toString());
            final String oldMd5 = SystemUtils.md5(file);
            if (!TextUtils.isEmpty(newMd5) && newMd5.equals(oldMd5)) {
                return null;
            }
        }
        properties.export(file);

        final List<String> cmd = createBaseSyncCommand();
        cmd.add(file.getCanonicalPath());
        final String destParent = SystemUtils.createPath(getConfiguration().getRemoteConfig().getDestinationPath(), destDirName);
        final String remotePath = rsyncRemotePath(destParent, filename);
        cmd.add(remotePath);
        return cmd;
    }

}

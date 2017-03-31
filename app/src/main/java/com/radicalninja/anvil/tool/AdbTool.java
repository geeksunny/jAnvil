package com.radicalninja.anvil.tool;

import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.config.ProjectConfig;
import com.radicalninja.anvil.util.ArrayUtils;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AdbTool extends Tool {

    // TODO: Needs support for connected devices by serial number. parse `adb devices` ?
    // TODO: Needs support for deploying to multiple device serials at a time
    // TODO: account for adb file-path vs system-path

    private static final String DEFAULT_ACTIVITY_TEMPLATE = "%s/%s.%s";

    public AdbTool(Configuration configuration) {
        super(configuration);
    }

    public void installApk(final File apkFile) {
        final List<String> cmd = startNewCommand();
        ArrayUtils.addAll(cmd, "install", "-r", apkFile.getAbsolutePath());
        SystemUtils.executeShellCommand(cmd);
    }

    public void startDefaultActivity() {
        final ProjectConfig config = getConfiguration().getProjectConfig();
        final String activity = String.format(DEFAULT_ACTIVITY_TEMPLATE,
                config.getProjectPackage(), config.getProjectPackage(), config.getStartActivity());
        startActivity(activity);
    }

    public void startActivity(final String activity) {
        final List<String> cmd = startNewCommand();
        ArrayUtils.addAll(cmd, "shell", "am", "start", "-n", activity);
        SystemUtils.executeShellCommand(cmd);
    }

    protected List<String> startNewCommand() {
        // TODO: Device serial numbers would be established here as '-s','device_serial'
        final List<String> command = new LinkedList<>();
        command.add("adb");
        return command;
    }

}

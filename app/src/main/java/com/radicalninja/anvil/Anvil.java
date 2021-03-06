package com.radicalninja.anvil;

import com.beust.jcommander.Parameter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.radicalninja.anvil.config.Configuration;
import com.radicalninja.anvil.tool.AdbTool;
import com.radicalninja.anvil.tool.BuildTool;
import com.radicalninja.anvil.tool.FilePuller;
import com.radicalninja.anvil.tool.SyncTool;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Anvil {

    public static Gson gson;

    private Arguments arguments;

    public Anvil(final Arguments arguments) {
        this.arguments = arguments;

        if (null == gson) {
            gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
        }
    }

    public Configuration loadConfig(final File file) throws IOException {
        final String json = SystemUtils.getFileContents(file);
        return gson.fromJson(json, Configuration.class);
    }

    public Configuration loadDefaultConfig() {
        // TODO: Should this be moved to
        final InputStream in = getClass().getResourceAsStream(Configuration.DEFAULT_FILENAME);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return gson.fromJson(reader, Configuration.class);
    }

    public void run() throws IOException, JSchException, SftpException, InterruptedException {
        // TODO: add a timer for the whole operation
        final Configuration configuration = loadConfig(new File("acmoore.json"));
        SystemUtils.setWorkingDirectory(configuration.getProjectConfig().getPath());

        final SyncTool syncTool = new SyncTool(configuration);
        syncTool.doSyncOperations();

        final BuildTool buildTool = new BuildTool(configuration);
        buildTool.executeGradleTask("assembleStagingDebug");

        final FilePuller filePuller = new FilePuller(configuration);
        final File buildResult = filePuller.pullBuildResult();

        final AdbTool adbTool = new AdbTool(configuration);
        adbTool.installApk(buildResult);
        adbTool.startDefaultActivity();
    }

    public static class Arguments {

        // todo: default paramaters should be gradle commands to be executed?
        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(names = {"-s", "--sync"}, description = "Sync your workspace files to the build server.")
        private boolean sync = false;   // todo: should this be reversed?

        @Parameter(names = {"--config", "-c"}, description = "Config file TODO")
        private String configFile;

        @Parameter(names = "--install", description = "Run the installer wizard to generate configuration files.")
        private boolean install;

        @Parameter(names = {"-h", "--help"}, help = true)
        private boolean help;
    }

}

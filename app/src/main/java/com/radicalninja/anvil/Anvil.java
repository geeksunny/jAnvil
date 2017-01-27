package com.radicalninja.anvil;

import com.beust.jcommander.Parameter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.radicalninja.anvil.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void run() throws IOException {
        final Map<String, String> propsToAdd = new HashMap<>();
        propsToAdd.put("com.radicalninja.anvil", "true");
        propsToAdd.put("com.radicalninja.anvil_", "false");
        propsToAdd.put("com.radicalninja.anvil__", "AWESOME");
        final String exportPath = "./local.properties";

        final File inFile = new File(SystemUtils.expandHomePath("~/.gradle/gradle.properties"));
        final GradleProperties properties = new GradleProperties(inFile);

        for (final Map.Entry<String, String> toAdd : propsToAdd.entrySet()) {
            properties.add(toAdd.getKey(), toAdd.getValue());
        }
        properties.export(new File(exportPath));
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

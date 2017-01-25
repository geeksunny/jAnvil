package com.radicalninja.anvil;

import com.beust.jcommander.Parameter;
import com.radicalninja.anvil.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anvil {

    // todo: default paramaters should be gradle commands to be executed?
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"-s", "--sync"}, description = "Sync your workspace files to the build server.")
    private boolean sync = false;   // todo: should this be reversed?

    @Parameter(names = {"--config", "-c"}, description = "Config file TODO")
    private String configFile;

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    public Anvil() {
        //
    }

    public void run() throws IOException {
        final Map<String, String> propsToAdd = new HashMap<>();
        propsToAdd.put("com.radicalninja.anvil", "true");
        propsToAdd.put("com.radicalninja.anvil_", "false");
        propsToAdd.put("com.radicalninja.anvil__", "AWESOME");
        final String exportPath = "./local.properties";

        final File inFile = new File(FileUtils.expandHomePath("~/.gradle/gradle.properties"));
        final GradleProperties properties = new GradleProperties(inFile);

        for (final Map.Entry<String, String> toAdd : propsToAdd.entrySet()) {
            properties.add(toAdd.getKey(), toAdd.getValue());
        }
        properties.export(new File(exportPath));
    }

}

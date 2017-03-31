package com.radicalninja.anvil.tool;

import com.radicalninja.anvil.config.Configuration;

public abstract class Tool {

    private final Configuration configuration;

    protected static String destDirName;

    public Tool(final Configuration configuration) {
        this.configuration = configuration;
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    // TODO: This should be an abstract class for BuildTool and FilePuller
    public interface SessionAware {
        boolean connect();
        boolean isConnected();
        boolean close();    // rename to disconnect() ?
    }

}

package com.radicalninja.anvil;

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

}

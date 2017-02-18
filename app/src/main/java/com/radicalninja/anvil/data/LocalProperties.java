package com.radicalninja.anvil.data;

import com.radicalninja.anvil.config.GradleConfig;

import java.io.IOException;

public class LocalProperties extends Properties<GradleConfig> {

    public LocalProperties(GradleConfig config) throws IOException {
        super(config);
    }

    @Override
    protected void parseConfig(GradleConfig config) {
        set(config.getLocalPropertiesContents());
    }

}

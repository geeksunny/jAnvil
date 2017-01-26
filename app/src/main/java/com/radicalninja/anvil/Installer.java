package com.radicalninja.anvil;

import com.radicalninja.anvil.config.*;

public class Installer {

    public Configuration defaultConfig() {
        // TODO: How to manage values fed into builder?
        final AnvilConfig.Builder anvilConfig = AnvilConfig.Builder.anAnvilConfig();
        final RemoteConfig.Builder remoteConfig = RemoteConfig.Builder.aRemoteConfig();
        final ProjectConfig.Builder projectConfig = ProjectConfig.Builder.aProjectConfig();
        final GradleConfig.Builder gradleConfig = GradleConfig.Builder.aGradleConfig();

        return Configuration.Builder.aConfiguration()
                .withAnvilConfig(anvilConfig.build())
                .withRemoteConfig(remoteConfig.build())
                .withProjectConfig(projectConfig.build())
                .withGradleConfig(gradleConfig.build())
                .build();
    }

}

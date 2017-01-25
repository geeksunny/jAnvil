package com.radicalninja.anvil.config;

import java.util.List;
import java.util.Map;

public class AnvilConfig {

    public class RemoteConfig {
        private String user;
        private String server;
        private int port;
        private String publicKey;
        private String destinationPath;
    }

    public class GradleConfig {
        private String propertiesPathLocal;
        private String propertiesPathRemote;
        private Map<String, String> gradlePropertiesAdd;
        private List<String> gradlePropertiesRemove;
        private String localPropertiesPath;
        private Map<String, String> localPropertiesContents;
        private String buildWrapperFilename;
    }

}

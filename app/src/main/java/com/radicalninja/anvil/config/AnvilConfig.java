package com.radicalninja.anvil.config;

public class AnvilConfig {

    private String localPropertiesPath;

    public String getLocalPropertiesPath() {
        return localPropertiesPath;
    }

    public static final class Builder {
        private String localPropertiesPath;

        private Builder() {
        }

        public static Builder anAnvilConfig() {
            return new Builder();
        }

        public Builder withLocalPropertiesPath(String localPropertiesPath) {
            this.localPropertiesPath = localPropertiesPath;
            return this;
        }

        public AnvilConfig build() {
            AnvilConfig anvilConfig = new AnvilConfig();
            anvilConfig.localPropertiesPath = this.localPropertiesPath;
            return anvilConfig;
        }
    }
}

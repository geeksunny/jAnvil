package com.radicalninja.anvil.config;

public class AnvilConfig implements Configuration.Config {

    private boolean useUserProperties;
    private boolean useProjectProperties;

    public boolean useUserProperties() {
        return useUserProperties;
    }

    public boolean useProjectProperties() {
        return useProjectProperties;
    }

    public static final class Builder {
        private boolean useUserProperties;
        private boolean useProjectProperties;

        private Builder() {
        }

        public static Builder anAnvilConfig() {
            return new Builder();
        }

        public Builder withUseUserProperties(boolean useUserProperties) {
            this.useUserProperties = useUserProperties;
            return this;
        }

        public Builder withUseProjectProperties(boolean useProjectProperties) {
            this.useProjectProperties = useProjectProperties;
            return this;
        }

        public AnvilConfig build() {
            AnvilConfig anvilConfig = new AnvilConfig();
            anvilConfig.useUserProperties = this.useUserProperties;
            anvilConfig.useProjectProperties = this.useProjectProperties;
            return anvilConfig;
        }
    }
}

package com.radicalninja.anvil.config;

public class AnvilConfig implements Configuration.Config {

    private String tempDirName;
    private boolean useUserProperties;
    private boolean useProjectProperties;
    private String adbBinaryPath;

    public String getTempDirName() {
        return tempDirName;
    }

    public boolean useUserProperties() {
        return useUserProperties;
    }

    public boolean useProjectProperties() {
        return useProjectProperties;
    }

    public String getAdbBinaryPath() {
        return adbBinaryPath;
    }

    public static final class Builder {
        private String tempDirName;
        private boolean useUserProperties;
        private boolean useProjectProperties;
        private String adbBinaryPath;

        private Builder() {
        }

        public static Builder anAnvilConfig() {
            return new Builder();
        }

        public Builder withTempDirName(String tempDirName) {
            this.tempDirName = tempDirName;
            return this;
        }

        public Builder withUseUserProperties(boolean useUserProperties) {
            this.useUserProperties = useUserProperties;
            return this;
        }

        public Builder withUseProjectProperties(boolean useProjectProperties) {
            this.useProjectProperties = useProjectProperties;
            return this;
        }

        public Builder withAdbBinaryPath(String adbBinaryPath) {
            this.adbBinaryPath = adbBinaryPath;
            return this;
        }

        public AnvilConfig build() {
            AnvilConfig anvilConfig = new AnvilConfig();
            anvilConfig.tempDirName = this.tempDirName;
            anvilConfig.useUserProperties = this.useUserProperties;
            anvilConfig.useProjectProperties = this.useProjectProperties;
            anvilConfig.adbBinaryPath = this.adbBinaryPath;
            return anvilConfig;
        }
    }
}

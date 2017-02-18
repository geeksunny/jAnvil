package com.radicalninja.anvil.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Configuration {

    public interface Config {
        //
    }

    @SerializedName("anvil")
    private AnvilConfig anvilConfig;

    @SerializedName("remote")
    private RemoteConfig remoteConfig;

    @SerializedName("project")
    private ProjectConfig projectConfig;

    // TODO: Should gradle config be within project?
    @SerializedName("gradle")
    private GradleConfig gradleConfig;

    public AnvilConfig getAnvilConfig() {
        return anvilConfig;
    }

    public RemoteConfig getRemoteConfig() {
        return remoteConfig;
    }

    public ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    public GradleConfig getGradleConfig() {
        return gradleConfig;
    }

    public static final class Builder {
        private AnvilConfig anvilConfig;
        private RemoteConfig remoteConfig;
        private ProjectConfig projectConfig;
        // TODO: Should gradle config be within project?
        private GradleConfig gradleConfig;

        private Builder() {
        }

        public static Builder aConfiguration() {
            return new Builder();
        }

        public Builder withAnvilConfig(AnvilConfig anvilConfig) {
            this.anvilConfig = anvilConfig;
            return this;
        }

        public Builder withRemoteConfig(RemoteConfig remoteConfig) {
            this.remoteConfig = remoteConfig;
            return this;
        }

        public Builder withProjectConfig(ProjectConfig projectConfig) {
            this.projectConfig = projectConfig;
            return this;
        }

        public Builder withGradleConfig(GradleConfig gradleConfig) {
            this.gradleConfig = gradleConfig;
            return this;
        }

        public Configuration build() {
            Configuration configuration = new Configuration();
            configuration.gradleConfig = this.gradleConfig;
            configuration.anvilConfig = this.anvilConfig;
            configuration.remoteConfig = this.remoteConfig;
            configuration.projectConfig = this.projectConfig;
            return configuration;
        }
    }
}

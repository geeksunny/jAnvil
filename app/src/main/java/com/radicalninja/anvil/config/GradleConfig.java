package com.radicalninja.anvil.config;

import java.util.List;
import java.util.Map;

public class GradleConfig implements Configuration.Config {

    private String userPropertiesPath;
    private String projectPropertiesFilename;
    private String projectLocalPropertiesFilename;
    private Map<String, String> projectPropertiesAdd;
    private List<String> projectPropertiesRemove;
    private Map<String, String> projectLocalPropertiesContents;
    private String buildWrapperFilename;

    public String getPropertiesPathLocal() {
        return userPropertiesPath;
    }

    public String getPropertiesPathRemote() {
        return projectPropertiesFilename;
    }

    public String getProjectLocalPropertiesFilename() {
        return projectLocalPropertiesFilename;
    }

    public Map<String, String> getGradlePropertiesAdd() {
        return projectPropertiesAdd;
    }

    public List<String> getGradlePropertiesRemove() {
        return projectPropertiesRemove;
    }

    public Map<String, String> getLocalPropertiesContents() {
        return projectLocalPropertiesContents;
    }

    public String getBuildWrapperFilename() {
        return buildWrapperFilename;
    }


    public static final class Builder {
        private String userPropertiesPath;
        private String projectPropertiesFilename;
        private String projectLocalPropertiesFilename;
        private Map<String, String> projectPropertiesAdd;
        private List<String> projectPropertiesRemove;
        private Map<String, String> projectLocalPropertiesContents;
        private String buildWrapperFilename;

        private Builder() {
        }

        public static Builder aGradleConfig() {
            return new Builder();
        }

        public Builder withUserPropertiesPath(String userPropertiesPath) {
            this.userPropertiesPath = userPropertiesPath;
            return this;
        }

        public Builder withProjectPropertiesFilename(String projectPropertiesFilename) {
            this.projectPropertiesFilename = projectPropertiesFilename;
            return this;
        }

        public Builder withProjectLocalPropertiesFilename(String projectLocalPropertiesFilename) {
            this.projectLocalPropertiesFilename = projectLocalPropertiesFilename;
            return this;
        }

        public Builder withProjectPropertiesAdd(Map<String, String> projectPropertiesAdd) {
            this.projectPropertiesAdd = projectPropertiesAdd;
            return this;
        }

        public Builder withProjectPropertiesRemove(List<String> projectPropertiesRemove) {
            this.projectPropertiesRemove = projectPropertiesRemove;
            return this;
        }

        public Builder withProjectLocalPropertiesContents(Map<String, String> projectLocalPropertiesContents) {
            this.projectLocalPropertiesContents = projectLocalPropertiesContents;
            return this;
        }

        public Builder withBuildWrapperFilename(String buildWrapperFilename) {
            this.buildWrapperFilename = buildWrapperFilename;
            return this;
        }

        public GradleConfig build() {
            GradleConfig gradleConfig = new GradleConfig();
            gradleConfig.projectPropertiesAdd = this.projectPropertiesAdd;
            gradleConfig.projectLocalPropertiesContents = this.projectLocalPropertiesContents;
            gradleConfig.projectPropertiesFilename = this.projectPropertiesFilename;
            gradleConfig.buildWrapperFilename = this.buildWrapperFilename;
            gradleConfig.userPropertiesPath = this.userPropertiesPath;
            gradleConfig.projectPropertiesRemove = this.projectPropertiesRemove;
            gradleConfig.projectLocalPropertiesFilename = this.projectLocalPropertiesFilename;
            return gradleConfig;
        }
    }
}

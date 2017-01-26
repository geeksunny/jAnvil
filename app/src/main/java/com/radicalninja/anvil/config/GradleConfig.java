package com.radicalninja.anvil.config;

import java.util.List;
import java.util.Map;

public class GradleConfig {

    private String propertiesPathLocal;
    private String propertiesPathRemote;
    private Map<String, String> gradlePropertiesAdd;
    private List<String> gradlePropertiesRemove;
    private Map<String, String> localPropertiesContents;
    private String buildWrapperFilename;

    public String getPropertiesPathLocal() {
        return propertiesPathLocal;
    }

    public String getPropertiesPathRemote() {
        return propertiesPathRemote;
    }

    public Map<String, String> getGradlePropertiesAdd() {
        return gradlePropertiesAdd;
    }

    public List<String> getGradlePropertiesRemove() {
        return gradlePropertiesRemove;
    }

    public Map<String, String> getLocalPropertiesContents() {
        return localPropertiesContents;
    }

    public String getBuildWrapperFilename() {
        return buildWrapperFilename;
    }

    public static final class Builder {
        private String propertiesPathLocal;
        private String propertiesPathRemote;
        private Map<String, String> gradlePropertiesAdd;
        private List<String> gradlePropertiesRemove;
        private Map<String, String> localPropertiesContents;
        private String buildWrapperFilename;

        private Builder() {
        }

        public static Builder aGradleConfig() {
            return new Builder();
        }

        public Builder withPropertiesPathLocal(String propertiesPathLocal) {
            this.propertiesPathLocal = propertiesPathLocal;
            return this;
        }

        public Builder withPropertiesPathRemote(String propertiesPathRemote) {
            this.propertiesPathRemote = propertiesPathRemote;
            return this;
        }

        public Builder withGradlePropertiesAdd(Map<String, String> gradlePropertiesAdd) {
            this.gradlePropertiesAdd = gradlePropertiesAdd;
            return this;
        }

        public Builder withGradlePropertiesRemove(List<String> gradlePropertiesRemove) {
            this.gradlePropertiesRemove = gradlePropertiesRemove;
            return this;
        }

        public Builder withLocalPropertiesContents(Map<String, String> localPropertiesContents) {
            this.localPropertiesContents = localPropertiesContents;
            return this;
        }

        public Builder withBuildWrapperFilename(String buildWrapperFilename) {
            this.buildWrapperFilename = buildWrapperFilename;
            return this;
        }

        public GradleConfig build() {
            GradleConfig gradleConfig = new GradleConfig();
            gradleConfig.gradlePropertiesAdd = this.gradlePropertiesAdd;
            gradleConfig.gradlePropertiesRemove = this.gradlePropertiesRemove;
            gradleConfig.propertiesPathRemote = this.propertiesPathRemote;
            gradleConfig.buildWrapperFilename = this.buildWrapperFilename;
            gradleConfig.localPropertiesContents = this.localPropertiesContents;
            gradleConfig.propertiesPathLocal = this.propertiesPathLocal;
            return gradleConfig;
        }
    }
}

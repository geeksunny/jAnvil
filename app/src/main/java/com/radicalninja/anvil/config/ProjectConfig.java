package com.radicalninja.anvil.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectConfig {

    // Project file location
    private String parentDirectory;
    private String directoryName;
    @SerializedName("package")
    private String projectPackage;
    private String startActivity;
    // remote
    private String remoteResultDirectory;
    private String remoteResultFile;
    // rsync
    private List<String> excludeFromFiles;
    private List<String> excludeFiles;
    // gradle
    private String gradleBuildWrapperTask;

    public static final class Builder {
        // Project file location
        private String parentDirectory;
        private String directoryName;
        private String projectPackage;
        private String startActivity;
        // remote
        private String remoteResultDirectory;
        private String remoteResultFile;
        // rsync
        private List<String> excludeFromFiles;
        private List<String> excludeFiles;
        // gradle
        private String gradleBuildWrapperTask;

        private Builder() {
        }

        public static Builder aProjectConfig() {
            return new Builder();
        }

        public Builder withParentDirectory(String parentDirectory) {
            this.parentDirectory = parentDirectory;
            return this;
        }

        public Builder withDirectoryName(String directoryName) {
            this.directoryName = directoryName;
            return this;
        }

        public Builder withProjectPackage(String projectPackage) {
            this.projectPackage = projectPackage;
            return this;
        }

        public Builder withStartActivity(String startActivity) {
            this.startActivity = startActivity;
            return this;
        }

        public Builder withRemoteResultDirectory(String remoteResultDirectory) {
            this.remoteResultDirectory = remoteResultDirectory;
            return this;
        }

        public Builder withRemoteResultFile(String remoteResultFile) {
            this.remoteResultFile = remoteResultFile;
            return this;
        }

        public Builder withExcludeFromFiles(List<String> excludeFromFiles) {
            this.excludeFromFiles = excludeFromFiles;
            return this;
        }

        public Builder withExcludeFiles(List<String> excludeFiles) {
            this.excludeFiles = excludeFiles;
            return this;
        }

        public Builder withGradleBuildWrapperTask(String gradleBuildWrapperTask) {
            this.gradleBuildWrapperTask = gradleBuildWrapperTask;
            return this;
        }

        public ProjectConfig build() {
            ProjectConfig projectConfig = new ProjectConfig();
            projectConfig.startActivity = this.startActivity;
            projectConfig.gradleBuildWrapperTask = this.gradleBuildWrapperTask;
            projectConfig.projectPackage = this.projectPackage;
            projectConfig.parentDirectory = this.parentDirectory;
            projectConfig.excludeFromFiles = this.excludeFromFiles;
            projectConfig.remoteResultDirectory = this.remoteResultDirectory;
            projectConfig.remoteResultFile = this.remoteResultFile;
            projectConfig.excludeFiles = this.excludeFiles;
            projectConfig.directoryName = this.directoryName;
            return projectConfig;
        }
    }
}

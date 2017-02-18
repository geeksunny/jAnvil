package com.radicalninja.anvil.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectConfig implements Configuration.Config {

    // Project file location
    // TODO: Add some logic for ensuring there is a trailing slash.
    private String path;
    // TODO: Determine if the 'project_parent_dir' is still required, parse if so.
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

    public String getPath() {
        return path;
    }

    public String getProjectPackage() {
        return projectPackage;
    }

    public String getStartActivity() {
        return startActivity;
    }

    public String getRemoteResultDirectory() {
        return remoteResultDirectory;
    }

    public String getRemoteResultFile() {
        return remoteResultFile;
    }

    public List<String> getExcludeFromFiles() {
        return excludeFromFiles;
    }

    public List<String> getExcludeFiles() {
        return excludeFiles;
    }

    public String getGradleBuildWrapperTask() {
        return gradleBuildWrapperTask;
    }

    public static final class Builder {
        // Project file location
        private String path;
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

        public Builder withPath(String path) {
            this.path = path;
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
            projectConfig.excludeFromFiles = this.excludeFromFiles;
            projectConfig.remoteResultDirectory = this.remoteResultDirectory;
            projectConfig.remoteResultFile = this.remoteResultFile;
            projectConfig.excludeFiles = this.excludeFiles;
            projectConfig.path = this.path;
            return projectConfig;
        }
    }
}

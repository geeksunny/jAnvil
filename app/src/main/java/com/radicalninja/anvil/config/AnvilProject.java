package com.radicalninja.anvil.config;

import java.util.List;

public class AnvilProject {

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

}

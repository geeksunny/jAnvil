package com.radicalninja.anvil.util;

public class FileUtils {

    public static String expandHomePath(final String path) {
        return path.replace("~", System.getProperty("user.home"));
    }

}

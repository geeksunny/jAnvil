package com.radicalninja.anvil;

import java.io.File;

public class HomeFile extends File {

    public static String expandHomePath(final String path) {
        return path.replace("~", System.getProperty("user.home"));
    }

    public HomeFile(String pathname) {
        super(expandHomePath(pathname));
    }

    public HomeFile(String parent, String child) {
        super(expandHomePath(parent), child);
    }

}

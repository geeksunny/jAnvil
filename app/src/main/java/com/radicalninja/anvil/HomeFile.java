package com.radicalninja.anvil;

import java.io.File;
import java.net.URI;

public class HomeFile extends File {

    public static String expandHomePath(final String path) {
        return path.replace("~", System.getProperty("user.home"));
    }

    public HomeFile(String pathname) {
        super(expandHomePath(pathname));
    }

    public HomeFile(String parent, String child) {
        super(parent, child);
    }

    public HomeFile(File parent, String child) {
        super(parent, child);
    }

    public HomeFile(URI uri) {
        super(uri);
    }
}

package com.radicalninja.anvil;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SyncTool {

    public static final String RSYNC_PATH_TEMPLATE = "%s@%s:%s";

    /**
     * Generate a md5 hash of a given String.
     * @param content String to be hashed.
     * @return A String of the resulting md5 hash.
     */
    private String md5(final String content) {
        try {
            final MessageDigest m;
            m = MessageDigest.getInstance("MD5");
            m.update(content.getBytes());
            final BigInteger hash = new BigInteger(1, m.digest());
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a md5 hash of a given File.
     * @param file The file to be hashed.
     * @return A String of the resulting md5 hash.
     */
    private String md5(final File file) throws IOException {
        final String content = new String(Files.readAllBytes(file.toPath()));
        return md5(content);
    }

}

package com.radicalninja.anvil.util;

import com.radicalninja.anvil.HomeFile;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Consumer;

public class SystemUtils {

    private static File workingDirectory = new File(".");

    /**
     * Generate a md5 hash of a given String.
     * @param content String to be hashed.
     * @return A String of the resulting md5 hash.
     */
    public static String md5(final String content) {
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
    public static String md5(final File file) throws IOException {
        final String content = new String(Files.readAllBytes(file.toPath()));
        return md5(content);
    }

    /**
     * Execute a shell command. Output is printed to system console.
     * @param commandArguments
     */
    public static void executeShellCommand(final List<String> commandArguments) {
        executeShellCommand(commandArguments, workingDirectory);
    }

    /**
     * Execute a shell command.
     * @param commandArguments
     * @param dir
     */
    // TODO: Remove @NotNull here?
    public static void executeShellCommand(
            final List<String> commandArguments, final File dir) {

        if (ArrayUtils.isEmpty(commandArguments)) {
            // TODO : maybe return boolean false here since failstate?
            return;
        }
        try {
            final String[] args = new String[commandArguments.size()];
            commandArguments.toArray(args);
            final Process p = Runtime.getRuntime().exec(args, null, dir);
            final Thread outputThread = new Thread(new OutputReader(p.getInputStream()));
            final Thread errorThread = new Thread(new OutputReader(p.getErrorStream()));
            outputThread.start();
            errorThread.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createPath(final String parent, final String child) {
        final String pathParent = StringUtils.stripEnd(parent, "/");
        final String pathChild = StringUtils.stripStart(child, "/");
        return String.format("%s/%s", pathParent, pathChild);
    }

    public static String getFileContents(final File file) throws IOException {
        final StringBuilder sb = new StringBuilder();
        Files.lines(file.toPath()).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                sb.append(s);
            }
        });
        return sb.toString();
    }

    public static void setWorkingDirectory(final String path) {
        workingDirectory = new HomeFile(path);
    }

    public static class OutputReader implements Runnable {
        final InputStream inputStream;

        public OutputReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String text = null;
                while ((text = reader.readLine()) != null) {
                    System.out.println(text);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

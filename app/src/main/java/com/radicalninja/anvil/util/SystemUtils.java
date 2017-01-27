package com.radicalninja.anvil.util;

import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Consumer;

public class SystemUtils {

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

    public static String expandHomePath(final String path) {
        return path.replace("~", System.getProperty("user.home"));
    }

    /**
     * Execute a shell command. Output is printed to system console.
     * @param commandArguments
     */
    public static void executeShellCommand(final List<String> commandArguments) {
        final Consumer<String> consumer = System.out::print;
        executeShellCommand(commandArguments, consumer);
    }

    /**
     * Execute a shell command.
     * @param commandArguments
     * @param outputConsumer
     */
    public static void executeShellCommand(
            final List<String> commandArguments, @NotNull final Consumer<String> outputConsumer) {

        try {
            final String[] args = new String[commandArguments.size()];
            commandArguments.toArray(args);
            final Process p = Runtime.getRuntime().exec(args);
            p.waitFor();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            reader.lines().forEach(outputConsumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

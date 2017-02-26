package com.radicalninja.anvil.data;

import com.radicalninja.anvil.HomeFile;
import com.radicalninja.anvil.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class Properties<T extends Configuration.Config> {

    private static final String PARSE_REGEX = "^([^\\#\\s\\=]+)\\=([^\\n]+)$";  // flags: mg - m might not be necessary if read line by line

    private final Map<String, String> properties = new LinkedHashMap<>();

    public Properties(final T config) throws IOException {
        if (null != config) {
            parseConfig(config);
        }
    }

    public Properties(final String filePath) throws IOException {
        parseFile(filePath);
    }

    public Properties(final File file) throws IOException {
        parseFile(file);
    }

    protected abstract void parseConfig(final T config) throws IOException;

    public void parseFile(final String filePath) throws IOException {
        parseFile(new HomeFile(filePath));
    }

    public void parseFile(final File file) throws IOException {
        final Pattern pattern = Pattern.compile(PARSE_REGEX);

        final Stream<String> lines = Files.lines(file.toPath());
        lines.forEach(s -> {
            final Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                final String key = matcher.group(1);
                final String value = matcher.group(2);
                add(key, value);
            }
        });
        lines.close();
    }

    public void set(final Map<String, String> contents) {
        if (null != contents) {
            properties.clear();
            properties.putAll(contents);
        }
    }

    public void add(final String key, final String value) {
        properties.put(key, value);
    }

    public void remove(final String key) {
        properties.remove(key);
    }

    public void export(final File destination) throws IOException {
        Files.write(destination.toPath(), getExportLines(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private String makeLine(final String key, final String value) {
        return String.format("%s=%s", key, value);
    }

    protected List<String> getExportLines() {
        final List<String> lines = new LinkedList<>();
        for (final Map.Entry<String, String> property : properties.entrySet()) {
            final String line = makeLine(property.getKey(), property.getValue());
            lines.add(line);
        }
        return lines;
    }

    @Override
    public String toString() {
        return String.join("\n", getExportLines());
    }
}

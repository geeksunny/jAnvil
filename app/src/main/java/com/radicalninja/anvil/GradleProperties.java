package com.radicalninja.anvil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GradleProperties {

    private static final String PARSE_REGEX = "^([^\\#\\s\\=]+)\\=([^\\n]+)$";  // flags: mg - m might not be necessary if read line by line
    private static final String EXPORT_HEADER = "# auto-generated gradle properties";

    private final Map<String, String> properties = new LinkedHashMap<>();

    public GradleProperties(final String filePath) throws IOException {
        this(new File(filePath));
    }

    public GradleProperties(final File file) throws IOException {
        final Pattern pattern = Pattern.compile(PARSE_REGEX);

        final Stream<String> lines = Files.lines(file.toPath());
        lines.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                final Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    final String key = matcher.group(1);
                    final String value = matcher.group(2);
                    properties.put(key, value);
                }
            }
        });
        lines.close();
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

    private List<String> getExportLines() {
        final List<String> lines = new LinkedList<>();
        lines.add(EXPORT_HEADER);
        for (final Map.Entry<String, String> property : properties.entrySet()) {
            final String line = makeLine(property.getKey(), property.getValue());
            lines.add(line);
        }
        return lines;
    }

}

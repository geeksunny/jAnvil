package com.radicalninja.anvil.data;

import com.radicalninja.anvil.config.GradleConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GradleProperties extends Properties<GradleConfig> {

    private static final String EXPORT_HEADER = "# auto-generated gradle properties";

    public GradleProperties(GradleConfig config) throws IOException{
        super(config);
    }

    public GradleProperties(String filePath) throws IOException {
        super(filePath);
    }

    public GradleProperties(File file) throws IOException {
        super(file);
    }

    @Override
    protected void parseConfig(GradleConfig config) throws IOException {
        if (null == config || null == config.getPropertiesPathLocal()) {
            return;
        }
        parseFile(config.getPropertiesPathLocal());
        final List<String> remove = config.getGradlePropertiesRemove();
        if (null != remove) {
            for (final String prop : remove) {
                remove(prop);
            }
        }
        final Map<String, String> add = config.getGradlePropertiesAdd();
        if (null != add) {
            for (final Map.Entry<String, String> prop : add.entrySet()) {
                add(prop.getKey(), prop.getValue());
            }
        }
    }

    @Override
    protected List<String> getExportLines() {
        final List<String> lines = super.getExportLines();
        lines.add(0, EXPORT_HEADER);
        return lines;
    }

}

package ranchercontrol.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public enum CliParser {;

    public static Properties loadProperties(final String filename) throws IOException {
        final Properties props = new Properties();
        try (final Reader reader = new FileReader(orDefault(filename, ".env"))) {
            props.load(reader);
        }
        return props;
    }

    public static String orDefault(final String value, final String defaultValue) {
        return value == null ? defaultValue : value;
    }

}

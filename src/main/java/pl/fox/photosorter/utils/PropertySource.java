package pl.fox.photosorter.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertySource {

    private static final String PROPERTIES_FILE = "/env.properties";

    private static final Properties properties = loadProperties();

    public static String getProperty(String key) {
        try {
            return properties.getProperty(key);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Could not find property " + key);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private static Properties loadProperties() {
        try (var in = PropertySource.class.getResourceAsStream(PROPERTIES_FILE)) {
            var properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException | NullPointerException ie) {
            throw new IllegalStateException("Could not load properties file", ie);
        }
    }

}

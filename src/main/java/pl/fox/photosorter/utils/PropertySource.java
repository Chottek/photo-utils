package pl.fox.photosorter.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertySource {

    private static final String PROPERTIES_FILE = System.getProperty("configFile", "/env.properties");
    private static final Properties PROPERTIES = loadProperties();

    public static String getProperty(String key) {
        var property = PROPERTIES.getProperty(key);

        if (property == null) {
            throw new IllegalArgumentException("Could not find property [" + key + "]");
        }

        if (property.isEmpty()) {
            throw new IllegalArgumentException("Value for property [" + key + "] is empty, please specify correct value in " + PROPERTIES_FILE + " file");
        }

        return property;
    }

    public static Integer getPropertyAsInt(String key, Integer defaultValue) {
        return Integer.parseInt(PROPERTIES.getProperty(key, defaultValue.toString()));
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
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

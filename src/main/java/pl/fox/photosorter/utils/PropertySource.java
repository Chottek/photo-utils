package pl.fox.photosorter.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertySource {

    private static final String PROPERTIES_FILE = System.getenv().getOrDefault(
            "config.file", "/env.properties"
    );
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

    public static void overrideProperty(String key, String value) {
        if (PROPERTIES.containsKey(key)) {
            PROPERTIES.setProperty(key, value);
            System.out.println("Property [" + key + "] overridden");
        } else {
            System.err.println("Property [" + key + "] not found, unable to override");
        }
    }

    private static Properties loadProperties() {
        try (var in = PropertySource.class.getResourceAsStream(PROPERTIES_FILE)) {
            var properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException | NullPointerException ie) {
            throw new IllegalStateException("Could not load properties file " + PROPERTIES_FILE, ie);
        }
    }

    // TODO: If we're getting the Timezone properties, we need to parse it later and add if statement to dateExtractor
    public static Properties getGroupProperties(String groupKey) {
        var properties = new Properties();
        PROPERTIES.entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith(groupKey))
                .forEach(entry -> properties.setProperty(entry.getKey().toString().substring(entry.getKey().toString().indexOf(groupKey + 1)), entry.getValue().toString()));
        return properties;
    }

    public static void printProperties() {
        PROPERTIES.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
    }

}

package pl.fox.photosorter.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class PropertySource {

    private static String PROPERTIES_FILE = System.getenv().getOrDefault(
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

    private static Optional<File> getOtherPropertyFile() {
        var isJar = ClassLoader.getSystemResource("pl/fox/photosorter/Main.class").toString().startsWith("jar");

        File dir = isJar ? new File("./") : new File("./src/main/resources/");

        File[] files = dir.listFiles((d, name) -> name.endsWith(".properties"));

        return Arrays.stream(files)
                .filter(File::isFile)
                .filter(e -> !e.getName().equals(PROPERTIES_FILE.replace("/", "")))
                .findFirst();
    }

    private static Properties loadProperties() {
        var properties = new Properties();
        getOtherPropertyFile().ifPresentOrElse(
                file -> {
                    System.out.println("Found an external property file, falling to " + file.getName());
                    PROPERTIES_FILE = file.getAbsolutePath();
                    try (InputStream is = Files.newInputStream(Path.of(file.getPath()))) {
                        properties.load(is);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try (var in = PropertySource.class.getResourceAsStream(PROPERTIES_FILE)) {
                        properties.load(in);
                    } catch (IOException | NullPointerException ie) {
                        throw new IllegalStateException("Could not load properties file " + PROPERTIES_FILE, ie);
                    }
                }
        );

        return properties;
    }

    public static Properties getGroupProperties(String groupKey) {
        var properties = new Properties();
        PROPERTIES.entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith(groupKey))
                .forEach(entry -> {
                    var keyName = entry.getKey().toString().substring(groupKey.length() + 1);
                    properties.setProperty(keyName, entry.getValue().toString());
                });
        return properties;
    }

    public static void printProperties() {
        PROPERTIES.forEach((k, v) -> System.out.println("[" + k + "] = " + v));
    }

}

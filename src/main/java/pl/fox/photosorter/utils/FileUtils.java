package pl.fox.photosorter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class FileUtils {

    private static final String[] allowedExtensions = parseAllowedExtensions();

    private String outputDirName;

    public void createOutputDirectory() {
        var isCustomOutputEnabled = parseBoolean(getProperty("customOutputEnabled"));

        outputDirName = isCustomOutputEnabled ?
                getProperty("outputPath") :
                getProperty("inputPath") + "_OUTPUT";

        if (!new File(outputDirName).exists()) {
            var hasBeenCreated = new File(outputDirName).mkdirs();
            System.out.println("Output directory (" + outputDirName + ") created: " + hasBeenCreated);
        }
    }

    public List<File> getFiles() {
        var input = getProperty("inputPath");
        int maxDepth = Integer.parseInt(getProperty("maxDepth"));

        try(Stream<Path> paths = Files.walk(Paths.get(input), maxDepth)) {
            var filesList =  paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(this::hasAllowedExtension)
                    .toList();
            System.out.println("Found " + filesList.size() + " files");
            return filesList;
        } catch (IOException ie) {
            throw new IllegalStateException("Cannot load image files from input path = " + input, ie);
        }
    }

    private boolean hasAllowedExtension(File file) {
        return Arrays.stream(allowedExtensions)
                .anyMatch(extension -> file.getName().toUpperCase().endsWith(extension));
    }

    private static String[] parseAllowedExtensions() {
        var allowedExtensionsProperty = getProperty("allowedExtensions");
        return Arrays.stream(allowedExtensionsProperty.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

}

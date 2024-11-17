package pl.fox.photosorter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class FileUtils {

    private static final String[] allowedExtensions = parseAllowedExtensions();

    private final ErrorHandler errorHandler = ErrorHandler.getInstance();

    private String outputDirName;

    public String getOutputFileName(File source, String newFilename) {
       return outputDirName + "/" + newFilename + "." + getExtension(source.getName());
    }

    public void copyFileToDestination(File file, String fileName) {
        var filename = fileName + "." + getExtension(file.getName());
        try {
            copyFile(file, new File(outputDirName + "/" + filename));
        } catch (IOException ex) {
            errorHandler.addErroredFile(file, "Unable to copy file to destination");
            System.err.println("Unable to copy file " + filename + " to " + outputDirName);
        }
    }

    public String createOutputDirectory() {
        var isCustomOutputEnabled = parseBoolean(getProperty("customOutputEnabled"));

        outputDirName = isCustomOutputEnabled ?
                getProperty("outputPath") :
                getProperty("inputPath") + "_OUTPUT";

        if (!new File(outputDirName).exists()) {
            var hasBeenCreated = new File(outputDirName).mkdirs();
            System.out.println("Output directory (" + outputDirName + ") created: " + hasBeenCreated);
        }

        return outputDirName;
    }

    public List<File> getFiles() {
        var input = getProperty("inputPath");
        int maxDepth = Integer.parseInt(getProperty("maxDepth"));

        try (Stream<Path> paths = Files.walk(Paths.get(input), maxDepth)) {
            var filesList = paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(this::hasAllowedExtension)
                    .toList();
            System.out.println("Found " + filesList.size() + " files");
            return filesList;
        } catch (IOException ie) {
            throw new IllegalStateException("Cannot load image files from input path = " + input, ie);
        }
    }

    public void appendOptionalSuffix(Map<File, String> fileMap, String optionalString) {
        if (optionalString == null || optionalString.isEmpty()) {
            return;
        }

        fileMap.entrySet().forEach(entry ->
                entry.setValue(entry.getValue() + "_" + optionalString)
        );
    }


    private boolean hasAllowedExtension(File file) {
        return Arrays.stream(allowedExtensions)
                .anyMatch(extension -> file.getName().toUpperCase().endsWith(extension));
    }

    private static String[] parseAllowedExtensions() {
        var allowedExtensionsProperty = getProperty("allowedExtensions", "JPG");
        return Arrays.stream(allowedExtensionsProperty.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

}

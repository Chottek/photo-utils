package pl.fox.photosorter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileLister {

    private static final String[] allowedExtensions = parseAllowedExtensions();
    
    public List<File> getFiles() throws IOException {
        var input = PropertySource.getProperty("inputPath");
        
        try(Stream<Path> paths = Files.walk(Paths.get(input))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(this::hasAllowedExtension)
                    .toList();
        } catch (IOException ie) {
            throw new IOException(ie);
        }
    }
    
    private boolean hasAllowedExtension(File file) {
        return Arrays.stream(allowedExtensions)
                .anyMatch(extension -> file.getName().toUpperCase().endsWith(extension));
    }
    
    private static String[] parseAllowedExtensions() {
        var allowedExtensionsProperty = PropertySource.getProperty("allowedExtensions");
        return Arrays.stream(allowedExtensionsProperty.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
    
}

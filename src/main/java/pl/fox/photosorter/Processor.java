package pl.fox.photosorter;

import pl.fox.photosorter.utils.extractors.DateExtractor;
import pl.fox.photosorter.utils.FileLister;
import pl.fox.photosorter.utils.extractors.Extractor;

import java.io.File;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;
import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class Processor {

    private final Extractor dateExtractor = new DateExtractor();

    private String outputDirName;

    public void run() {
        final var files = new FileLister().getFiles();
        final var fileMap = files.stream().collect(Collectors.toMap(
                file -> file,
                dateExtractor::extract
        ));

        createOutputDirectory();
    }

    private void createOutputDirectory() {
        var isCustomOutputEnabled = parseBoolean(getProperty("customOutputEnabled"));

        outputDirName = isCustomOutputEnabled ?
                getProperty("outputPath") :
                getProperty("inputPath") + "_OUTPUT";

        var hasBeenCreated = new File(outputDirName).mkdirs();
        System.out.println("Output directory (" + outputDirName + ") created: " + hasBeenCreated);
    }

}

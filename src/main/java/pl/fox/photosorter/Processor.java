package pl.fox.photosorter;

import pl.fox.photosorter.utils.FileUtils;
import pl.fox.photosorter.utils.extractors.DateExtractor;
import pl.fox.photosorter.utils.extractors.Extractor;

import java.util.stream.Collectors;

public class Processor {

    private final FileUtils fileUtils = new FileUtils();
    private final Extractor dateExtractor = new DateExtractor();

    private String outputDirName;

    public void run() {
        final var files = fileUtils.getFiles();
        final var fileMap = files.stream().collect(Collectors.toMap(
                file -> file,
                dateExtractor::extract
        ));

        fileUtils.createOutputDirectory();
    }

}

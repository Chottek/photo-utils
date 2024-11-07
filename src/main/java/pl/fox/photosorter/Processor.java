package pl.fox.photosorter;

import pl.fox.photosorter.utils.ErrorHandler;
import pl.fox.photosorter.utils.FileUtils;
import pl.fox.photosorter.utils.extractors.DateExtractor;
import pl.fox.photosorter.utils.extractors.Extractor;

import java.io.File;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static pl.fox.photosorter.utils.DuplicateHandler.handleDuplicates;

public class Processor {

    private final ErrorHandler errorHandler = new ErrorHandler();
    private final FileUtils fileUtils = new FileUtils();
    private final Extractor dateExtractor = new DateExtractor();

    public void run() {
        var filesList = fileUtils.getFiles();
        if (filesList.isEmpty()) {
            throw new IllegalArgumentException("No files found!");
        }

        var fileMap = new HashMap<File, String>();
        for (File file : filesList) {
            try {
                fileMap.put(file, dateExtractor.extract(file));
            } catch (NoSuchElementException nse) {
                errorHandler.addErroredFile(file, "Failed to properly parse metadata");
            }
        }

        handleDuplicates(fileMap);

        fileUtils.createOutputDirectory();

        fileMap.forEach(fileUtils::copyFileToDestination);
    }

}

package pl.fox.photosorter;

import pl.fox.photosorter.utils.DateUtils;
import pl.fox.photosorter.utils.DuplicateHandler;
import pl.fox.photosorter.utils.ErrorHandler;
import pl.fox.photosorter.utils.FileUtils;
import pl.fox.photosorter.utils.extractors.DateExtractor;
import pl.fox.photosorter.utils.extractors.Extractor;
import pl.fox.photosorter.utils.extractors.NameExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static pl.fox.photosorter.utils.MapDistributor.distribute;
import static pl.fox.photosorter.utils.PropertySource.getProperty;
import static pl.fox.photosorter.utils.PropertySource.getPropertyAsInt;

public class Processor {

    private final ErrorHandler errorHandler = new ErrorHandler();
    private final FileUtils fileUtils = new FileUtils();
    private final Extractor nameExtractor = new NameExtractor();
    private final Extractor dateExtractor = new DateExtractor();
    private final DuplicateHandler duplicateHandler = new DuplicateHandler();
    private final DateUtils dateUtils = new DateUtils();

    public void run() {
        var filesList = fileUtils.getFiles();
        if (filesList.isEmpty()) {
            throw new IllegalArgumentException("No files found!");
        }

        var fileMap = new HashMap<File, String>();
        for (File file : filesList) {
            try {
                var cameraName = nameExtractor.extract(file);
                var date = dateExtractor.extract(file);
                var formattedDate = dateUtils.formatDate(date, cameraName);
                fileMap.put(file, formattedDate);
            } catch (NoSuchElementException nse) {
                errorHandler.addErroredFile(file, "Failed to properly parse metadata");
            }
        }

        fileUtils.appendOptionalSuffix(fileMap, getProperty("optionalSuffix", null));
        duplicateHandler.handleDuplicates(fileMap);

        var outputDirName = fileUtils.createOutputDirectory();

        if (fileMap.size() < getPropertyAsInt("concurrenceThreshold", 1000)) {
            fileMap.forEach(fileUtils::copyFileToDestination); //Sync
        } else {
            startCopyPool(fileMap, outputDirName); //Async
        }

        System.out.println("Files copied successfully.");
        errorHandler.printErroredFiles();
    }

    private void startCopyPool(Map<File, String> fileMap, String outputDirName) {
        var poolSize = getPropertyAsInt("maxPoolSize", 4);
        List<Map<File, String>> distributedMaps = distribute(fileMap);

        try(ExecutorService executorService = Executors.newFixedThreadPool(poolSize)) {
            List<Future<?>> futures = new ArrayList<>();

            for (Map<File, String> fileGroup : distributedMaps) {
                Future<?> future = executorService.submit(new FileCopyTask(fileUtils, fileGroup));
                futures.add(future);
            }

            for (Future<?> future : futures) {
                future.get();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

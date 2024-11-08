package pl.fox.photosorter;

import pl.fox.photosorter.utils.ErrorHandler;
import pl.fox.photosorter.utils.FileUtils;
import pl.fox.photosorter.utils.extractors.DateExtractor;
import pl.fox.photosorter.utils.extractors.Extractor;

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

import static pl.fox.photosorter.utils.DuplicateHandler.handleDuplicates;
import static pl.fox.photosorter.utils.MapDistributor.distribute;
import static pl.fox.photosorter.utils.PropertySource.getPropertyAsInt;

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
                Future<?> future = executorService.submit(new FileCopyTask(fileGroup, outputDirName));
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

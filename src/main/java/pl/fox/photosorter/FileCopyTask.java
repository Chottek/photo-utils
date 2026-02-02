package pl.fox.photosorter;

import pl.fox.photosorter.utils.ErrorHandler;
import pl.fox.photosorter.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.io.FileUtils.copyFile;
import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class FileCopyTask implements Runnable {

    private final FileUtils fileUtils;
    private final ErrorHandler errorHandler = ErrorHandler.getInstance();
    private final Map<File, String> fileMap;

    public FileCopyTask(FileUtils fileUtils, Map<File, String> fileMap) {
        System.out.println("Scheduled new FileCopyTask for " + fileMap.size() + " files");
        this.fileUtils = fileUtils;
        this.fileMap = fileMap;
    }

    @Override
    public void run() {
        System.out.println("Started " + this + "(" + fileMap.size() + ") files");
        for (Map.Entry<File, String> entry : fileMap.entrySet()) {
            File source = entry.getKey();

            fileUtils.copyFileToDestination(source, entry.getValue());
        }

        System.out.println(this + " -> FINISHED");
    }

}
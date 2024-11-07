package pl.fox.photosorter;

import pl.fox.photosorter.utils.ErrorHandler;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class FileCopyTask implements Runnable {

    private final ErrorHandler errorHandler = ErrorHandler.getInstance();
    private final Map<File, String> fileMap;
    private final String outputPath;

    public FileCopyTask(Map<File, String> fileMap, String outputPath) {
        System.out.println("Scheduled new FileCopyTask for " + fileMap.size() + " files");
        this.fileMap = fileMap;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        System.out.println("Started " + this + "(" + fileMap.size() + ") files");
        for (Map.Entry<File, String> entry : fileMap.entrySet()) {
            File source = entry.getKey();
            File destination = new File(outputPath + "/" + entry.getValue() + "." + getExtension(source.getName()));

            try {
                copyFile(source, destination);
            } catch (IOException e) {
                System.err.println("Failed to copy " + source.getName() + " to " + destination.getName());
                errorHandler.addErroredFile(entry.getKey(), "Failed to copy file to destination folder");
            }
        }

        System.out.println(this + " -> FINISHED");
    }

}
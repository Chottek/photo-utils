package pl.fox.photosorter;

import pl.fox.photosorter.utils.FileLister;

import java.io.File;

import static java.lang.Boolean.parseBoolean;
import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class Processor {

    private String outputDirName;

    public void run() {
        final var files = new FileLister().getFiles();
        createOutputDirectory();
        
    }

    private void createOutputDirectory() {
        var isCustomOutputEnabled = parseBoolean(getProperty("customOutputEnabled"));

        outputDirName = isCustomOutputEnabled ?
                getProperty("outputPath") :
                getProperty("inputPath") + "_OUTPUT";

        var hasBeenCreated = new File(outputDirName).mkdirs();
        System.out.println("Output directory created: " + hasBeenCreated);
    }

}

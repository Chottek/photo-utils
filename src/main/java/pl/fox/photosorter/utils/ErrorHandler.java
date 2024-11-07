package pl.fox.photosorter.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorHandler {

    private static ErrorHandler instance;
    private final Map<String, List<File>> errorMap = new HashMap<>();

    public ErrorHandler() {
        if (instance == null) {
            instance = this;
        }
    }

    public void addErroredFile(File file, String reason) {
        if (!errorMap.containsKey(reason)) {
            errorMap.put(reason, new ArrayList<>(List.of(file)));
        } else {
            errorMap.get(reason).add(file);
        }
    }

    //TODO: Maybe copy the errored into separate folder called <INPUT_NAME>_ERRORS ??
    public void printErroredFiles() {
        if (errorMap.isEmpty()) {
            return;
        }

        System.out.println("ERRORS:");
        errorMap.forEach((key, value) -> {
            System.out.println("Reason: [" + key + "] " + value);
        });
    }

    public static ErrorHandler getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ErrorHandler has not been initialized");
        }
        return instance;
    }
}

package pl.fox.photosorter.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DuplicateHandler {

    public void handleDuplicates(Map<File, String> fileMap) {
        var c = fileMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
                .collect(Collectors.toMap(
                        item -> item.getFirst().getValue(),
                        item -> new ArrayList<>(
                                item.stream()
                                        .map(Map.Entry::getKey)
                                        .toList()
                        ))
                );

        var duplicates = new HashMap<>(c).entrySet().stream()
                .filter(duplicateList -> duplicateList.getValue().size() > 1)
                .toList();

        var handledDuplicatesMap = new HashMap<File, String>();
        for(var duplicate: duplicates) {
            for (int i = 1; i < duplicate.getValue().size(); i++) {
                handledDuplicatesMap.put(duplicate.getValue().get(i), duplicate.getKey() + "_" + i);
            }
        }

        fileMap.putAll(handledDuplicatesMap);
    }

}

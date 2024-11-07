package pl.fox.photosorter.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapDistributor {

    public static List<Map<File, String>> distribute(Map<File, String> fileMap) {
        int maxMapThreshold = PropertySource.getPropertyAsInt("singleThreadFileThreshold", 250);
        List<Map<File, String>> distributedMaps = new ArrayList<>();
        Map<File, String> tempMap = new HashMap<>();

        for (Map.Entry<File, String> entry : fileMap.entrySet()) {
            tempMap.put(entry.getKey(), entry.getValue());

            if (tempMap.size() == maxMapThreshold) {
                distributedMaps.add(new HashMap<>(tempMap));
                tempMap.clear();
            }
        }

        if (!tempMap.isEmpty()) {
            distributedMaps.add(tempMap);
        }

        return distributedMaps;
    }

}

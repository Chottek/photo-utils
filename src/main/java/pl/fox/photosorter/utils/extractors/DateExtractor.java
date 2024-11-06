package pl.fox.photosorter.utils.extractors;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class DateExtractor extends Extractor {

    private static final List<Integer> exifSourcesDirectories = List.of(36867);

    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            getProperty("dateFormat", "yyyyMMdd_HHmmss")
    );

    @Override
    public String extract(File file) {
        var date = getDateFromMetadata(file).orElseThrow();
        Date inputFormatted;
        try {
            inputFormatted = INPUT_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date", e);
        }
        return DATE_FORMATTER.format(inputFormatted);
    }

    private Optional<String> getDateFromMetadata(File file) {
        return getMetadataDirectories(file).stream()
                .map(clazz -> clazz.getString(exifSourcesDirectories.getFirst()))
                .filter(Objects::nonNull)
                .findFirst();
    }

//    private static Optional<String> getDateFromMetadata(File file) {
//        return getMetadataDirectories(file).stream()
//                .map(clazz -> {
//                    var exifCode = exifDateCodes.get(clazz.getClass().getSimpleName());
//                    return clazz.getString(exifCode);
//                })
//                .filter(Objects::nonNull)
//                .findFirst();
//    }


    //    private static List<? extends ExifDirectoryBase> getMetadataIFD0Directories(File file) {
//        var metadata = readMetadata(file);
//        var s = ((ArrayList) metadata.getDirectories()).stream()
//                .map(e -> {
//                    try{
//                        return e;
//                    } catch(ClassCastException c) {
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .map(e -> e.getClass().getSimpleName()).toList();
//
//        return s;
//    }

}

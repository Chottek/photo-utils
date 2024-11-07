package pl.fox.photosorter.utils.extractors;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class DateExtractor extends Extractor {

    private static final List<Integer> exifSourcesDirectories = List.of(36867);

    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            getProperty("dateFormat", "yyyyMMdd_HHmmss")
    );

    @Override
    public String extract(File file) {
        var date = getDateFromMetadata(file).orElseThrow(); //Catch this later and gather in list of errors
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

}

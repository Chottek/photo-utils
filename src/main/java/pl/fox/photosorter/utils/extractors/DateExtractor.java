package pl.fox.photosorter.utils.extractors;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class DateExtractor extends Extractor {

    private static final Integer EXIF_SOURCE_DIR = 36867;

    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            getProperty("dateFormat", "yyyyMMdd_HHmmss")
    );

    static {
        System.out.println("Current timeZone: " + DATE_FORMATTER.getTimeZone());
    }

    @Override
    public String extract(File file) {
        var date = getDateFromMetadata(file).orElseThrow();
        Date inputFormatted;
        try {
//           alignTimeZone(file);
            inputFormatted = INPUT_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date", e);
        }
        return DATE_FORMATTER.format(inputFormatted);
    }

    private Optional<String> getDateFromMetadata(File file) {
        return getMetadataDirectories(file).stream()
                .map(clazz -> clazz.getString(EXIF_SOURCE_DIR))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * This method is to be used to align timezones during the winter time if the CANON camera has not been changed
     * TODO: Make it configurable maybe, look for EXIF directory number having camera name and getting the ZoneID
     */
    private void alignTimeZone(File file) {
        if (file.getName().startsWith("IMG")) { //CANON NAME
            DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        } else {
            DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        }
    }

}

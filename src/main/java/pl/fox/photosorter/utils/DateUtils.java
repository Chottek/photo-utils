package pl.fox.photosorter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static pl.fox.photosorter.utils.PropertySource.getProperty;

public class DateUtils {

    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            getProperty("dateFormat", "yyyyMMdd_HHmmss")
    );

    static {
        System.out.println("Current timeZone: " + DATE_FORMATTER.getTimeZone());
    }

    public String formatDate(String date, String sourceName) {
        Date inputFormatted;
        try {
            inputFormatted = INPUT_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date", e);
        }

        alignTimeZone(sourceName);

        return DATE_FORMATTER.format(inputFormatted);
    }

    private void alignTimeZone(String sourceName) {
        var timezoneConfiguration = configurationForSource(sourceName);
        if (timezoneConfiguration != null) {
            DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone(timezoneConfiguration));
        } else {
            DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        }
    }

    private String configurationForSource(String sourceName) {
        var timezoneProperties = PropertySource.getGroupProperties("Timezone");
        var generalTimezoneProperty = timezoneProperties.getProperty("General");

        var timezoneSettings = "";
        if (sourceName != null) {
            timezoneSettings = (String) timezoneProperties.entrySet().stream()
                    .filter(timezoneProperty -> sourceName.startsWith(timezoneProperty.getKey().toString()))
                    .map(Map.Entry::getValue).findFirst().orElse(null);
        }

        if ((timezoneSettings == null && generalTimezoneProperty != null) ||
        (timezoneSettings.isEmpty() && generalTimezoneProperty != null)) {
            return generalTimezoneProperty;
        }

        if (timezoneSettings != null && timezoneSettings.isEmpty()) {
            return null;
        }

        return timezoneSettings;
    }

}

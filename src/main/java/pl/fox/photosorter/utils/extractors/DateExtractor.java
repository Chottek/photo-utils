package pl.fox.photosorter.utils.extractors;

import java.io.File;

public class DateExtractor extends Extractor {

    public DateExtractor() {
        this.exifSourceDirNumber = 36867;
    }

    @Override
    public String extract(File file) {
        return getValueFromMetadata(file).orElseThrow();
    }

}

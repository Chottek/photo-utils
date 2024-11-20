package pl.fox.photosorter.utils.extractors;

import java.io.File;

public class NameExtractor extends Extractor {

    public NameExtractor() {
        this.exifSourceDirNumber = 50708;
      //  this.exifSourceDirNumber = 42035;
    }

    @Override
    public String extract(File file) {
        return getValueFromMetadata(file).orElse(null);
    }


}

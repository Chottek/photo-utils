package pl.fox.photosorter.utils.extractors;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public abstract class Extractor {

    private static final List<Class<? extends ExifDirectoryBase>> exifSourcesDirectories = List.of(
            ExifSubIFDDirectory.class
    );

    public String extract(File file) {
        throw new UnsupportedOperationException("Not implemented in abstract class");
    }

    private Metadata readMetadata(File file) {
        try {
            return ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException | IOException e) {
            throw new RuntimeException("Unable to read metadata of file [" + file.getName() + "]", e);
        }
    }

    protected List<? extends ExifDirectoryBase> getMetadataDirectories(File file) {
        var metadata = readMetadata(file);
        return exifSourcesDirectories.stream()
                .map(metadata::getDirectoriesOfType)
                .flatMap(Collection::stream)
                .toList();
    }

}

# PhotoSorter

A small project to handle pictures and sort them by date when they were taken

---
### Configuration
`env.properties` file can be used to configure values
 
- input path
- maximum depth of directories for files scanning
- output path (if customOutputEnabled is set to true)
  - if it's disabled, then output folder will be created with name `<input_path>_OUTPUT` 
- allowed file extensions to be processed
- date format to be part of the filename

- concurrenceThreshold - amount of files determining whether sync or asynchronous approach should be used
- maxPoolSize - maximum size of the concurrent Thread Pool
- singleThreadFileThreshold - amount of files to be processed by single concurrent Thread

---
### Exif Metadata


This project uses **drewnoakes.metadata-extractor** \
Exif value of `36867` is used to grab the date from metadata \
More values can be found here -> https://exiv2.org/tags.html

---

### Changelog

**1.1**
- Added concurrent execution in case the file amount threshold is exceeded
  - Thread pool configuration
  - Map splitting in smaller pieces to be processed asynchronously

**1.0** 
- Working base
- Duplicate names handling
- Date extractor
- env.properties configuration file parsing
- Error handling


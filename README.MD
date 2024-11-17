# PhotoSorter

A small project to handle pictures and sort them by date when they were taken

---
### Configuration
`env.properties` file can be used to configure values.

Creating own `.properties` file is also possible. Remember to specify
`config.file=/<NAME_OF_PROPERTIES_FILE>` flag in environment variables in run configuration (slash is mandatory)
 
**Configurable values**: 
- input path
- maximum depth of directories for files scanning
- output path (if customOutputEnabled is set to true)
  - if it's disabled, then output folder will be created with name `<input_path>_OUTPUT` 
- allowed file extensions to be processed
- date format to be part of the filename
- optionalSuffix - additional string to be appended to the extracted date

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

**1.3** TODO:
- Adding possibility to copy ERRORed files to _ERROR directory

**1.2** 
- Added possibility to specify own configuration file in `resources` folder

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


package pl.fox.photosorter;

import pl.fox.photosorter.utils.PropertySource;

import static pl.fox.photosorter.utils.ArgParser.parseArgs;
import static pl.fox.photosorter.utils.PropertySource.printProperties;

public class Main {

    public static void main(String[] args) {
        parseArgs(args).forEach((k, v) -> PropertySource.overrideProperty((String) k, (String) v));
        printProperties();
        new Processor().run();
    }

}
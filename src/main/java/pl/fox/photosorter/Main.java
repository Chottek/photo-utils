package pl.fox.photosorter;

import static pl.fox.photosorter.utils.ArgParser.parseArgs;
import static pl.fox.photosorter.utils.PropertySource.printProperties;

public class Main {

    public static void main(String[] args) {
        parseArgs(args);
        printProperties();
        new Processor().run();
    }

}
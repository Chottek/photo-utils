package pl.fox.photosorter;

import pl.fox.photosorter.utils.PropertySource;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
//        if (args != null && args.length > 0) {
//            System.out.println(Arrays.toString(args));
//            PropertySource.overrideProperty("inputPath", args[0]);
//        }

        PropertySource.printProperties();
        new Processor().run();
    }

}
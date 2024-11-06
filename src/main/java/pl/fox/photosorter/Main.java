package pl.fox.photosorter;

import pl.fox.photosorter.utils.PropertySource;

public class Main {

    public static void main(String[] args) {
        var prop = PropertySource.getProperty("inputPath");
        var prop1 = PropertySource.getProperty("allowedExtensions");
        System.out.println(prop);
        System.out.println(prop1);
    }

}
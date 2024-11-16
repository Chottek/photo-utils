package pl.fox.photosorter.utils;

import java.util.Arrays;

public class ArgParser {

    private static final String FLAG_PREFIX = "-D";

    public static void parseArgs(String[] args) {
        Arrays.stream(args)
                .filter(arg -> arg.startsWith(FLAG_PREFIX))
                .map(arg -> arg.split("="))
                .forEach(arg -> {
                    var property = arg[0].substring(FLAG_PREFIX.length()).trim();
                    PropertySource.overrideProperty(property, arg[1]);
                });
    }

}
